package anuong.cantho.backend.service.quanan.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.quanan.hinhanhquan.HinhAnhQuanResponseDTO;
import anuong.cantho.backend.entity.quanan.HinhAnhQuan;
import anuong.cantho.backend.entity.quanan.QuanAn;
import anuong.cantho.backend.repository.quanan.HinhAnhQuanRepository;
import anuong.cantho.backend.repository.quanan.QuanAnRepository;
import anuong.cantho.backend.service.quanan.HinhAnhQuanService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HinhAnhQuanServiceImpl implements HinhAnhQuanService{

  private final HinhAnhQuanRepository hinhAnhQuanRepository;
  private final QuanAnRepository quanAnRepository;

  @Value("${app.upload.dir:uploads}")
  private String uploadDir;

  private static final String THU_MUC_ANH = "hinh-anh-quan";

  private static final long DUNG_LUONG_TOI_DA = 10L * 1024 * 1024;

  private static final Map<String, String> DINH_DANG_ANH = Map.of(
      "image/jpeg", ".jpg",
      "image/png", ".png",
      "image/webp", ".webp",
      "image/gif", ".gif");

  // =========================
  // 1. LẤY TẤT CẢ HÌNH ẢNH
  // =========================
  @Override
  @Transactional (readOnly = true)
  public List<HinhAnhQuanResponseDTO> layTatCa() {

    return hinhAnhQuanRepository
        .findAllByOrderByThuTuAscIdAsc()
        .stream()
        .map(HinhAnhQuanResponseDTO::fromEntity)
        .toList();
  }

  // =========================
  // 2. LẤY ẢNH THEO QUÁN
  // =========================
  @Override
  @Transactional(readOnly = true)
  public List<HinhAnhQuanResponseDTO> layTheoQuanAn(Long quanAnId) {

    if (quanAnId == null || quanAnId <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID quán ăn không hợp lệ");
    }

    if (!quanAnRepository.existsById(quanAnId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy quán ăn");
    }

    return hinhAnhQuanRepository
        .findByQuanAn_IdOrderByThuTuAscIdAsc(quanAnId)
        .stream()
        .map(HinhAnhQuanResponseDTO::fromEntity)
        .toList();
  }

  // =========================
  // 3. TẢI LÊN MỘT HOẶC NHIỀU ẢNH
  // =========================
  @Override
  @Transactional
  public List<HinhAnhQuanResponseDTO> taiLen(
      Long quanAnId,
      MultipartFile[] files,
      String chuThich) {

    if (quanAnId == null || quanAnId <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID quán ăn không hợp lệ");
    }

    if (files == null || files.length == 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng chọn ít nhất một hình ảnh");
    }

    // Kiểm tra quán có tồn tại
    QuanAn quanAn = quanAnRepository.findById(quanAnId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy quán ăn"));

    Path thuMucLuuAnh = layThuMucLuuAnh();

    List<HinhAnhQuan> danhSachDaLuu = new ArrayList<>();
    List<Path> cacFileDaTao = new ArrayList<>();

    try {
      Files.createDirectories(thuMucLuuAnh);

      List<HinhAnhQuan> anhHienCo = hinhAnhQuanRepository
          .findByQuanAn_IdOrderByThuTuAscIdAsc(quanAnId);

      int thuTuBatDau = anhHienCo.stream()
          .map(HinhAnhQuan::getThuTu)
          .filter(thuTu -> thuTu != null)
          .mapToInt(Integer::intValue)
          .max()
          .orElse(-1) + 1;

      for (int i = 0; i < files.length; i++) {

        MultipartFile file = files[i];

        if (file == null || file.isEmpty()) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Có hình ảnh rỗng trong danh sách tải lên");
        }

        if (file.getSize() > DUNG_LUONG_TOI_DA) {
          throw new ResponseStatusException(
              HttpStatus.CONTENT_TOO_LARGE,
              "Mỗi hình ảnh không được vượt quá 10 MB");
        }

        String contentType = file.getContentType();

        String extension = DINH_DANG_ANH.get(contentType);

        if (extension == null) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Chỉ chấp nhận ảnh JPEG, PNG, WEBP hoặc GIF");
        }

        // Tên file ngẫu nhiên, không dùng trực tiếp tên file gốc
        String tenFile = UUID.randomUUID() + extension;

        Path duongDanFile = thuMucLuuAnh.resolve(tenFile).normalize();

        // Đảm bảo file luôn nằm trong thư mục được phép
        if (!duongDanFile.startsWith(thuMucLuuAnh)) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Đường dẫn file không hợp lệ");
        }

        file.transferTo(duongDanFile);
        cacFileDaTao.add(duongDanFile);

        HinhAnhQuan hinhAnh = new HinhAnhQuan();

        hinhAnh.setQuanAn(quanAn);

        // Lưu URL tương đối, không lưu đường dẫn tuyệt đối trên máy
        hinhAnh.setUrlAnh(
            "/uploads/" + THU_MUC_ANH + "/" + tenFile);

        hinhAnh.setLoaiAnh(contentType);

        hinhAnh.setChuThich(
            StringUtils.hasText(chuThich)
                ? chuThich.trim()
                : null);

        hinhAnh.setAnhDaiDien(false);
        hinhAnh.setThuTu(thuTuBatDau + i);

        danhSachDaLuu.add(hinhAnh);
      }

      List<HinhAnhQuan> ketQua = hinhAnhQuanRepository.saveAll(danhSachDaLuu);

      return ketQua.stream()
          .map(HinhAnhQuanResponseDTO::fromEntity)
          .toList();

    } catch (ResponseStatusException e) {

      xoaFileDaTao(cacFileDaTao);
      throw e;

    } catch (IOException e) {

      xoaFileDaTao(cacFileDaTao);

      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Không thể lưu hình ảnh lên máy chủ",
          e);

    } catch (RuntimeException e) {

      xoaFileDaTao(cacFileDaTao);
      throw e;
    }
  }

  // =========================
  // 4. CHỌN ẢNH ĐẠI DIỆN
  // =========================
  @Override
  @Transactional
  public HinhAnhQuanResponseDTO datAnhDaiDien(Long id) {

    HinhAnhQuan anhDuocChon = hinhAnhQuanRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy hình ảnh"));

    Long quanAnId = anhDuocChon.getQuanAn().getId();

    // Bỏ ảnh đại diện cũ của cùng quán
    List<HinhAnhQuan> anhDaiDienCu = hinhAnhQuanRepository
        .findByQuanAn_IdAndAnhDaiDienTrue(quanAnId);

    for (HinhAnhQuan anh : anhDaiDienCu) {
      anh.setAnhDaiDien(false);
    }

    hinhAnhQuanRepository.saveAll(anhDaiDienCu);

    // Đặt ảnh được chọn làm ảnh đại diện
    anhDuocChon.setAnhDaiDien(true);

    HinhAnhQuan ketQua = hinhAnhQuanRepository.save(anhDuocChon);

    return HinhAnhQuanResponseDTO.fromEntity(ketQua);
  }

  // =========================
  // 5. CẬP NHẬT THỨ TỰ ẢNH
  // =========================
  @Override
  @Transactional
  public HinhAnhQuanResponseDTO capNhatThuTu(
      Long id,
      Integer thuTu) {

    if (thuTu == null || thuTu < 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Thứ tự ảnh phải lớn hơn hoặc bằng 0");
    }

    HinhAnhQuan hinhAnh = hinhAnhQuanRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy hình ảnh"));

    hinhAnh.setThuTu(thuTu);

    HinhAnhQuan ketQua = hinhAnhQuanRepository.save(hinhAnh);

    return HinhAnhQuanResponseDTO.fromEntity(ketQua);
  }

  // =========================
  // 6. XÓA HÌNH ẢNH
  // =========================
  @Override
  @Transactional
  public void xoa(Long id) {

    HinhAnhQuan hinhAnh = hinhAnhQuanRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy hình ảnh"));

    Path fileCanXoa = layDuongDanFileTuUrl(
        hinhAnh.getUrlAnh());

    // Xóa bản ghi trong database
    hinhAnhQuanRepository.delete(hinhAnh);

    /*
     * Sau khi transaction commit thành công, thử xóa file.
     * Nếu không xóa được file, bản ghi DB vẫn đã bị xóa.
     */
    if (fileCanXoa != null) {
      xoaFileDaTao(List.of(fileCanXoa));
    }
  }

  // =========================
  // HÀM PHỤ: THƯ MỤC LƯU ẢNH
  // =========================
  private Path layThuMucLuuAnh() {

    return Paths.get(uploadDir)
        .toAbsolutePath()
        .normalize()
        .resolve(THU_MUC_ANH)
        .normalize();
  }

  // =========================
  // HÀM PHỤ: LẤY ĐƯỜNG DẪN FILE
  // =========================
  private Path layDuongDanFileTuUrl(String urlAnh) {

    if (!StringUtils.hasText(urlAnh)) {
      return null;
    }

    String tienTo = "/uploads/" + THU_MUC_ANH + "/";

    // Chỉ xóa file nằm trong khu vực quản lý ảnh quán ăn
    if (!urlAnh.startsWith(tienTo)) {
      return null;
    }

    String tenFile = urlAnh.substring(tienTo.length());

    // Không cho phép đường dẫn chứa thư mục con hoặc ký tự phân cách
    if (tenFile.isBlank()
        || tenFile.contains("/")
        || tenFile.contains("\\")
        || tenFile.equals(".")
        || tenFile.equals("..")) {
      return null;
    }

    Path thuMucLuuAnh = layThuMucLuuAnh();
    Path file = thuMucLuuAnh.resolve(tenFile).normalize();

    if (!file.startsWith(thuMucLuuAnh)) {
      return null;
    }

    return file;
  }

  // =========================
  // HÀM PHỤ: XÓA FILE
  // =========================
  private void xoaFileDaTao(List<Path> cacFile) {

    for (Path file : cacFile) {
      try {
        Files.deleteIfExists(file);
      } catch (IOException e) {
        // Không để lỗi xóa file che mất lỗi nghiệp vụ ban đầu
        System.err.println(
            "Không thể xóa file hình ảnh: " + file);
      }
    }
  }
}
