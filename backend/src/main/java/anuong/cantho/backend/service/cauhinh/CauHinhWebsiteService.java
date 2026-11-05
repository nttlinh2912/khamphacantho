package anuong.cantho.backend.service.cauhinh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.cauhinh.CauHinhWebsiteRequestDTO;
import anuong.cantho.backend.dto.cauhinh.CauHinhWebsiteResponseDTO;
import anuong.cantho.backend.entity.cauhinh.CauHinhWebsite;
import anuong.cantho.backend.repository.cauhinh.CauHinhWebsiteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CauHinhWebsiteService {

  private final CauHinhWebsiteRepository repository;

  // Thư mục lưu ảnh
  private final Path uploadDir = Paths.get(
      "uploads",
      "cau-hinh").toAbsolutePath().normalize();

  // Thư mục lưu banner trang chủ
  private final Path bannerUploadDir = Paths.get(
      "uploads",
      "banner").toAbsolutePath().normalize();

  // Định dạng ảnh được phép
  private static final Set<String> EXTENSIONS = Set.of(
      ".jpg",
      ".jpeg",
      ".png",
      ".webp",
      ".gif",
      ".ico");

  // Dung lượng tối đa 5MB
  private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

  // ==========================================
  // LẤY CẤU HÌNH
  // ==========================================

  @Transactional(readOnly = true)
  public CauHinhWebsiteResponseDTO layCauHinh() {

    CauHinhWebsite cauHinh = repository
        .findFirstByOrderByIdAsc()
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Chưa có cấu hình website"));

    return chuyenDoiResponse(cauHinh);
  }

  // ==========================================
  // CẬP NHẬT CẤU HÌNH
  // ==========================================

  @Transactional
  public CauHinhWebsiteResponseDTO capNhatCauHinh(
      CauHinhWebsiteRequestDTO request) {

    CauHinhWebsite cauHinh = repository
        .findFirstByOrderByIdAsc()
        .orElseGet(CauHinhWebsite::new);

    // Thương hiệu
    cauHinh.setTenWebsite(request.getTenWebsite());
    cauHinh.setSlogan(request.getSlogan());

    // Banner trang chủ
    cauHinh.setBannerTrangChu(request.getBannerTrangChu());
    cauHinh.setBannerAlt(request.getBannerAlt());
    cauHinh.setBannerTieuDe(request.getBannerTieuDe());
    cauHinh.setBannerMoTa(request.getBannerMoTa());
    cauHinh.setBannerLink(request.getBannerLink());

    if (request.getHienThiBanner() != null) {
      cauHinh.setHienThiBanner(request.getHienThiBanner());
    }

    // Liên hệ
    cauHinh.setEmail(request.getEmail());
    cauHinh.setSoDienThoai(request.getSoDienThoai());
    cauHinh.setDiaChi(request.getDiaChi());

    // Mạng xã hội
    cauHinh.setFacebook(request.getFacebook());
    cauHinh.setZalo(request.getZalo());
    cauHinh.setYoutube(request.getYoutube());
    cauHinh.setInstagram(request.getInstagram());
    cauHinh.setTiktok(request.getTiktok());

    // Theo dõi
    cauHinh.setMaGoogleAnalytics(
        request.getMaGoogleAnalytics());

    cauHinh.setMaGoogleSearchConsole(
        request.getMaGoogleSearchConsole());

    cauHinh.setMaFacebookPixel(
        request.getMaFacebookPixel());

    // SEO
    cauHinh.setTieuDeSeoTrangChu(
        request.getTieuDeSeoTrangChu());

    cauHinh.setMoTaSeoTrangChu(
        request.getMoTaSeoTrangChu());

    cauHinh.setTuKhoaSeoTrangChu(
        request.getTuKhoaSeoTrangChu());

    // Footer
    cauHinh.setChanTrangWebsite(
        request.getChanTrangWebsite());

    CauHinhWebsite saved = repository.save(cauHinh);

    return chuyenDoiResponse(saved);
  }

  // ==========================================
  // UPLOAD BANNER TRANG CHỦ
  // ==========================================

  @Transactional
  public CauHinhWebsiteResponseDTO uploadBanner(
      MultipartFile file) {

    CauHinhWebsite cauHinh = layHoacTaoCauHinh();

    // Lấy đường dẫn banner cũ
    String urlCu = cauHinh.getBannerTrangChu();

    // Lưu ảnh banner mới
    String urlMoi = luuBanner(file);

    // Cập nhật đường dẫn banner
    cauHinh.setBannerTrangChu(urlMoi);

    // Lưu cấu hình
    CauHinhWebsite saved = repository.save(cauHinh);

    // Xóa ảnh banner cũ sau khi lưu ảnh mới
    xoaBannerCu(urlCu);

    return chuyenDoiResponse(saved);
  }

  // ==========================================
  // UPLOAD LOGO
  // ==========================================

  @Transactional
  public CauHinhWebsiteResponseDTO uploadLogo(
      MultipartFile file) {

    CauHinhWebsite cauHinh = layHoacTaoCauHinh();

    String urlCu = cauHinh.getLogo();

    String urlMoi = luuFile(file);

    cauHinh.setLogo(urlMoi);

    CauHinhWebsite saved = repository.save(cauHinh);

    xoaFileCu(urlCu);

    return chuyenDoiResponse(saved);
  }

  // ==========================================
  // UPLOAD FAVICON
  // ==========================================

  @Transactional
  public CauHinhWebsiteResponseDTO uploadFavicon(
      MultipartFile file) {

    CauHinhWebsite cauHinh = layHoacTaoCauHinh();

    String urlCu = cauHinh.getFavicon();

    String urlMoi = luuFile(file);

    cauHinh.setFavicon(urlMoi);

    CauHinhWebsite saved = repository.save(cauHinh);

    xoaFileCu(urlCu);

    return chuyenDoiResponse(saved);
  }

  // ==========================================
  // LẤY HOẶC TẠO CẤU HÌNH
  // ==========================================

  private CauHinhWebsite layHoacTaoCauHinh() {

    return repository
        .findFirstByOrderByIdAsc()
        .orElseGet(() -> {

          CauHinhWebsite cauHinh = new CauHinhWebsite();

          cauHinh.setTenWebsite(
              "Địa điểm ăn uống Cần Thơ");

          return repository.save(cauHinh);
        });
  }

  // ==========================================
  // LƯU FILE
  // ==========================================

  private String luuFile(MultipartFile file) {

    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng chọn hình ảnh");
    }

    if (file.getSize() > MAX_FILE_SIZE) {
      throw new ResponseStatusException(
          HttpStatus.CONTENT_TOO_LARGE,
          "Dung lượng hình ảnh không được vượt quá 5MB");
    }

    String contentType = file.getContentType();

    if (contentType == null ||
        !contentType.toLowerCase().startsWith("image/")) {

      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "File tải lên phải là hình ảnh");
    }

    String originalName = file.getOriginalFilename();

    if (originalName == null || originalName.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Tên file không hợp lệ");
    }

    String fileName = Paths.get(originalName)
        .getFileName()
        .toString();

    int lastDot = fileName.lastIndexOf(".");

    if (lastDot < 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "File không có phần mở rộng");
    }

    String extension = fileName
        .substring(lastDot)
        .toLowerCase();

    if (!EXTENSIONS.contains(extension)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Định dạng ảnh không được hỗ trợ");
    }

    try {

      Files.createDirectories(uploadDir);

      String newFileName = UUID.randomUUID() + extension;

      Path destination = uploadDir
          .resolve(newFileName)
          .normalize();

      if (!destination.startsWith(uploadDir)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Đường dẫn file không hợp lệ");
      }

      Files.copy(file.getInputStream(), destination);

      return "/uploads/cau-hinh/" + newFileName;

    } catch (IOException e) {

      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Không thể lưu hình ảnh");
    }
  }

  // ==========================================
  // LƯU FILE BANNER
  // ==========================================

  private String luuBanner(MultipartFile file) {

    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng chọn hình ảnh banner");
    }

    if (file.getSize() > MAX_FILE_SIZE) {
      throw new ResponseStatusException(
          HttpStatus.CONTENT_TOO_LARGE,
          "Dung lượng banner không được vượt quá 5MB");
    }

    String contentType = file.getContentType();

    if (contentType == null ||
        !contentType.toLowerCase().startsWith("image/")) {

      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "File tải lên phải là hình ảnh");
    }

    String originalName = file.getOriginalFilename();

    if (originalName == null || originalName.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Tên file không hợp lệ");
    }

    String fileName = Paths.get(originalName)
        .getFileName()
        .toString();

    int lastDot = fileName.lastIndexOf(".");

    if (lastDot < 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "File không có phần mở rộng");
    }

    String extension = fileName
        .substring(lastDot)
        .toLowerCase();

    if (!EXTENSIONS.contains(extension) ||
        extension.equals(".ico")) {

      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Banner chỉ hỗ trợ JPG, JPEG, PNG, WEBP hoặc GIF");
    }

    try {

      Files.createDirectories(bannerUploadDir);

      String newFileName = UUID.randomUUID() + extension;

      Path destination = bannerUploadDir
          .resolve(newFileName)
          .normalize();

      if (!destination.startsWith(bannerUploadDir)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Đường dẫn file không hợp lệ");
      }

      Files.copy(file.getInputStream(), destination);

      return "/uploads/banner/" + newFileName;

    } catch (IOException e) {

      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          "Không thể lưu hình ảnh banner");
    }
  }

  // ==========================================
  // XÓA FILE CŨ
  // ==========================================

  private void xoaFileCu(String urlCu) {

    if (urlCu == null || urlCu.isBlank()) {
      return;
    }

    String prefix = "/uploads/cau-hinh/";

    if (!urlCu.startsWith(prefix)) {
      return;
    }

    try {

      String fileName = urlCu.substring(prefix.length());

      Path filePath = uploadDir
          .resolve(fileName)
          .normalize();

      if (filePath.startsWith(uploadDir)) {
        Files.deleteIfExists(filePath);
      }

    } catch (IOException e) {

      // Không làm thất bại thao tác cập nhật
      // chỉ vì không xóa được ảnh cũ.
    }
  }

  // ==========================================
  // XÓA BANNER CŨ
  // ==========================================

  private void xoaBannerCu(String urlCu) {

    if (urlCu == null || urlCu.isBlank()) {
      return;
    }

    String prefix = "/uploads/banner/";

    if (!urlCu.startsWith(prefix)) {
      return;
    }

    try {

      String fileName = urlCu.substring(prefix.length());

      Path filePath = bannerUploadDir
          .resolve(fileName)
          .normalize();

      if (filePath.startsWith(bannerUploadDir)) {
        Files.deleteIfExists(filePath);
      }

    } catch (IOException e) {

      // Không làm thất bại thao tác cập nhật
      // chỉ vì không xóa được banner cũ.
    }
  }

  // ==========================================
  // CHUYỂN ENTITY SANG RESPONSE DTO
  // ==========================================

  // ==========================================
// CHUYỂN ENTITY SANG RESPONSE DTO
// ==========================================

private CauHinhWebsiteResponseDTO chuyenDoiResponse(
    CauHinhWebsite c) {

  return new CauHinhWebsiteResponseDTO(

      c.getId(),

      // Thương hiệu
      c.getTenWebsite(),
      c.getSlogan(),
      c.getLogo(),
      c.getFavicon(),

      // Banner trang chủ
      c.getBannerTrangChu(),
      c.getBannerAlt(),
      c.getBannerTieuDe(),
      c.getBannerMoTa(),
      c.getBannerLink(),
      c.getHienThiBanner(),

      // Liên hệ
      c.getEmail(),
      c.getSoDienThoai(),
      c.getDiaChi(),

      // Mạng xã hội
      c.getFacebook(),
      c.getZalo(),
      c.getYoutube(),
      c.getInstagram(),
      c.getTiktok(),

      // Theo dõi
      c.getMaGoogleAnalytics(),
      c.getMaGoogleSearchConsole(),
      c.getMaFacebookPixel(),

      // SEO
      c.getTieuDeSeoTrangChu(),
      c.getMoTaSeoTrangChu(),
      c.getTuKhoaSeoTrangChu(),

      // Footer
      c.getChanTrangWebsite());
}

}
