package anuong.cantho.backend.service.baiviet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.baiviet.hinhanhbaiviet.CapNhatThuTuHinhAnhDTO;
import anuong.cantho.backend.dto.baiviet.hinhanhbaiviet.HinhAnhBaiVietRequestDTO;
import anuong.cantho.backend.dto.baiviet.hinhanhbaiviet.HinhAnhBaiVietResponseDTO;
import anuong.cantho.backend.entity.baiviet.BaiViet;
import anuong.cantho.backend.entity.baiviet.HinhAnhBaiViet;
import anuong.cantho.backend.repository.baiviet.BaiVietRepository;
import anuong.cantho.backend.repository.baiviet.HinhAnhBaiVietRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HinhAnhBaiVietService {

  private final BaiVietRepository baiVietRepository;
  private final HinhAnhBaiVietRepository hinhAnhRepository;

  @Value("${app.upload.root:uploads}")
  private String uploadRoot;

  private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;

  private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "webp", "gif");

  private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
      "image/jpeg",
      "image/png",
      "image/webp",
      "image/gif");

  // =====================================================
  // CHUYỂN ENTITY SANG RESPONSE DTO
  // =====================================================

  private HinhAnhBaiVietResponseDTO toResponseDTO(
      HinhAnhBaiViet hinhAnh) {
    return new HinhAnhBaiVietResponseDTO(
        hinhAnh.getId(),
        hinhAnh.getBaiViet().getId(),
        hinhAnh.getUrlAnh(),
        hinhAnh.getAltText(),
        hinhAnh.getChuThich(),
        hinhAnh.getThuTu());
  }

  // =====================================================
  // LẤY DANH SÁCH ẢNH
  // =====================================================

  @Transactional(readOnly = true)
  public List<HinhAnhBaiVietResponseDTO> getByBaiViet(
      Long baiVietId) {
    if (!baiVietRepository.existsById(baiVietId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy bài viết");
    }

    return hinhAnhRepository
        .findByBaiViet_IdOrderByThuTuAscIdAsc(baiVietId)
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  // =====================================================
  // UPLOAD NHIỀU ẢNH
  // =====================================================

  @Transactional
  public List<HinhAnhBaiVietResponseDTO> upload(
      Long baiVietId,
      List<MultipartFile> files) {
    BaiViet baiViet = baiVietRepository.findById(baiVietId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy bài viết"));

    if (files == null || files.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng chọn ít nhất một hình ảnh");
    }

    if (files.size() > 10) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Mỗi lần chỉ được tải lên tối đa 10 ảnh");
    }

    List<Path> filesDaLuu = new ArrayList<>();
    List<HinhAnhBaiViet> entitiesDaLuu = new ArrayList<>();

    try {
      int thuTuBatDau = (int) hinhAnhRepository
          .countByBaiViet_Id(baiVietId);

      LocalDate homNay = LocalDate.now();

      String nam = String.valueOf(homNay.getYear());

      Path thuMucNam = Paths.get(
          uploadRoot,
          "bai-viet",
          nam).toAbsolutePath().normalize();

      Files.createDirectories(thuMucNam);

      for (int i = 0; i < files.size(); i++) {

        MultipartFile file = files.get(i);

        kiemTraFile(file);

        String extension = layDuoiFile(file.getOriginalFilename());

        String tenFile = UUID.randomUUID()
            .toString()
            .replace("-", "")
            + "."
            + extension;

        Path filePath = thuMucNam.resolve(tenFile).normalize();

        // Không cho phép đường dẫn file vượt ra ngoài thư mục upload
        if (!filePath.startsWith(thuMucNam)) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Đường dẫn file không hợp lệ");
        }

        file.transferTo(filePath);

        filesDaLuu.add(filePath);

        String urlAnh = "/uploads/bai-viet/"
            + nam
            + "/"
            + tenFile;

        HinhAnhBaiViet hinhAnh = new HinhAnhBaiViet();

        hinhAnh.setBaiViet(baiViet);
        hinhAnh.setUrlAnh(urlAnh);
        hinhAnh.setAltText(null);
        hinhAnh.setChuThich(null);
        hinhAnh.setThuTu(thuTuBatDau + i);

        entitiesDaLuu.add(hinhAnhRepository.save(hinhAnh));
      }

      return entitiesDaLuu.stream()
          .map(this::toResponseDTO)
          .toList();

    } catch (IOException | RuntimeException ex) {

      // Xóa file vừa tải nếu có lỗi khi xử lý
      for (Path path : filesDaLuu) {
        try {
          Files.deleteIfExists(path);
        } catch (IOException ignored) {
          // Có thể ghi log ở đây khi triển khai thực tế
        }
      }

      if (ex instanceof ResponseStatusException responseException) {
        throw responseException;
      }

      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Không thể lưu hình ảnh",
          ex);
    }
  }

  // =====================================================
  // KIỂM TRA FILE ẢNH
  // =====================================================

  private void kiemTraFile(MultipartFile file) {

    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "File ảnh không được để trống");
    }

    if (file.getSize() > MAX_FILE_SIZE) {
      throw new ResponseStatusException(
          HttpStatus.CONTENT_TOO_LARGE,
          "Mỗi ảnh không được vượt quá 5 MB");
    }

    String extension = layDuoiFile(file.getOriginalFilename());

    if (!ALLOWED_EXTENSIONS.contains(extension)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Chỉ hỗ trợ ảnh JPG, JPEG, PNG, WEBP và GIF");
    }

    String contentType = file.getContentType();

    if (contentType == null
        || !ALLOWED_CONTENT_TYPES.contains(
            contentType.toLowerCase(Locale.ROOT))) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Định dạng nội dung file không phải hình ảnh được hỗ trợ");
    }
  }

  private String layDuoiFile(String tenFileGoc) {

    if (tenFileGoc == null || tenFileGoc.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Tên file không hợp lệ");
    }

    // Chỉ lấy phần tên sau dấu phân cách cuối cùng
    String tenSach = Paths.get(tenFileGoc)
        .getFileName()
        .toString();

    int viTriCham = tenSach.lastIndexOf('.');

    if (viTriCham < 0 || viTriCham == tenSach.length() - 1) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "File phải có phần mở rộng");
    }

    return tenSach.substring(viTriCham + 1)
        .toLowerCase(Locale.ROOT);
  }

  // =====================================================
  // CẬP NHẬT ALT, CHÚ THÍCH, THỨ TỰ
  // =====================================================

  @Transactional
  public HinhAnhBaiVietResponseDTO capNhat(
      Long baiVietId,
      Long hinhAnhId,
      HinhAnhBaiVietRequestDTO dto) {
    HinhAnhBaiViet hinhAnh = hinhAnhRepository.findById(hinhAnhId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy hình ảnh"));

    if (!hinhAnh.getBaiViet().getId().equals(baiVietId)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Hình ảnh không thuộc bài viết này");
    }

    hinhAnh.setAltText(dto.getAltText());
    hinhAnh.setChuThich(dto.getChuThich());

    if (dto.getThuTu() != null) {
      hinhAnh.setThuTu(dto.getThuTu());
    }

    return toResponseDTO(hinhAnhRepository.save(hinhAnh));
  }

  // =====================================================
  // CẬP NHẬT THỨ TỰ NHIỀU ẢNH
  // =====================================================

  @Transactional
  public List<HinhAnhBaiVietResponseDTO> capNhatThuTu(
      Long baiVietId,
      List<CapNhatThuTuHinhAnhDTO> danhSach) {
    if (!baiVietRepository.existsById(baiVietId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy bài viết");
    }

    if (danhSach == null || danhSach.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Danh sách thứ tự ảnh không được để trống");
    }

    for (CapNhatThuTuHinhAnhDTO dto : danhSach) {

      HinhAnhBaiViet hinhAnh = hinhAnhRepository
          .findById(dto.getId())
          .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "Không tìm thấy hình ảnh ID " + dto.getId()));

      if (!hinhAnh.getBaiViet().getId().equals(baiVietId)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Có hình ảnh không thuộc bài viết này");
      }

      hinhAnh.setThuTu(dto.getThuTu());
    }

    return hinhAnhRepository
        .findByBaiViet_IdOrderByThuTuAscIdAsc(baiVietId)
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  // =====================================================
  // XÓA ẢNH
  // =====================================================

  @Transactional
  public void xoa(Long baiVietId, Long hinhAnhId) {

    HinhAnhBaiViet hinhAnh = hinhAnhRepository.findById(hinhAnhId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy hình ảnh"));

    if (!hinhAnh.getBaiViet().getId().equals(baiVietId)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Hình ảnh không thuộc bài viết này");
    }

    Path filePath = layDuongDanFile(hinhAnh.getUrlAnh());

    hinhAnhRepository.delete(hinhAnh);

    // Chỉ xóa file nếu URL là file upload nội bộ hợp lệ
    if (filePath != null) {
      try {
        Files.deleteIfExists(filePath);
      } catch (IOException ex) {
        // Dữ liệu DB đã xóa; ghi log để xử lý file còn sót nếu cần
      }
    }
  }

  private Path layDuongDanFile(String urlAnh) {

    if (urlAnh == null
        || !urlAnh.startsWith("/uploads/bai-viet/")) {
      return null;
    }

    String phanConLai = urlAnh.substring("/uploads/".length());

    Path uploadRootPath = Paths.get(uploadRoot)
        .toAbsolutePath()
        .normalize();

    Path filePath = uploadRootPath.resolve(phanConLai)
        .normalize();

    if (!filePath.startsWith(
        uploadRootPath.resolve("bai-viet").normalize())) {
      return null;
    }

    return filePath;
  }

  @Transactional
  public void datLamAnhDaiDien(Long baiVietId, Long hinhAnhId) {

    BaiViet baiViet = baiVietRepository.findById(baiVietId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy bài viết"));

    HinhAnhBaiViet hinhAnh = hinhAnhRepository.findById(hinhAnhId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy hình ảnh"));

    if (!hinhAnh.getBaiViet().getId().equals(baiVietId)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Hình ảnh không thuộc bài viết này");
    }

    baiViet.setHinhDaiDien(hinhAnh.getUrlAnh());
    baiViet.setHinhDaiDienAlt(hinhAnh.getAltText());

    baiVietRepository.save(baiViet);
  }

}
