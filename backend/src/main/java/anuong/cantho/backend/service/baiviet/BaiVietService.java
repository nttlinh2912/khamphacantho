package anuong.cantho.backend.service.baiviet;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.baiviet.baiviet.BaiVietRequestDTO;
import anuong.cantho.backend.dto.baiviet.baiviet.BaiVietResponseDTO;
import anuong.cantho.backend.dto.baiviet.baiviet.CapNhatTrangThaiBaiVietDTO;
import anuong.cantho.backend.dto.baiviet.baiviet.KiemTraSlugBaiVietResponseDTO;
import anuong.cantho.backend.entity.baiviet.BaiViet;
import anuong.cantho.backend.entity.baiviet.DanhMucBaiViet;
import anuong.cantho.backend.repository.baiviet.BaiVietRepository;
import anuong.cantho.backend.repository.baiviet.DanhMucBaiVietRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BaiVietService {

  private final BaiVietRepository baiVietRepository;
  private final DanhMucBaiVietRepository danhMucRepository;

  private static final Pattern SLUG_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

  // =========================
  // CHUYỂN ENTITY SANG DTO
  // =========================

  private BaiVietResponseDTO toResponseDTO(BaiViet baiViet) {

    return new BaiVietResponseDTO(
        baiViet.getId(),

        baiViet.getDanhMucBaiViet().getId(),
        baiViet.getDanhMucBaiViet().getTenDanhMuc(),

        baiViet.getTieuDe(),
        baiViet.getDuongDanSeo(),
        baiViet.getTomTat(),
        baiViet.getNoiDung(),

        baiViet.getHinhDaiDien(),
        baiViet.getHinhDaiDienAlt(),

        baiViet.getTuKhoaChinh(),
        baiViet.getTieuDeSeo(),
        baiViet.getMoTaSeo(),
        baiViet.getTuKhoaSeo(),

        baiViet.getLuotXem(),

        baiViet.getHoatDong(),
        baiViet.getNoiBat(),

        baiViet.getNgayDang(),
        baiViet.getNgayTao(),
        baiViet.getNgayCapNhat());
  }

  // =========================
  // LẤY BÀI VIẾT THEO ID
  // =========================

  @Transactional(readOnly = true)
  public BaiVietResponseDTO getById(Long id) {

    BaiViet baiViet = baiVietRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy bài viết"));

    return toResponseDTO(baiViet);
  }

  // =========================
  // DANH SÁCH QUẢN TRỊ
  // =========================

  @Transactional(readOnly = true)
  public Page<BaiVietResponseDTO> getAll(
      String keyword,
      Long danhMucId,
      Boolean hoatDong,
      Boolean noiBat,
      Pageable pageable) {

    Page<BaiViet> danhSach = baiVietRepository.timKiemQuanTri(
        keyword,
        danhMucId,
        hoatDong,
        noiBat,
        pageable);

    return danhSach.map(this::toResponseDTO);
  }

  // =========================
  // THÊM BÀI VIẾT
  // =========================

  @Transactional
  public BaiVietResponseDTO create(BaiVietRequestDTO dto) {

    kiemTraSlugHopLe(dto.getDuongDanSeo(), null);

    DanhMucBaiViet danhMuc = layDanhMucHoatDong(dto.getDanhMucBaiVietId());

    BaiViet baiViet = new BaiViet();

    ganDuLieu(baiViet, dto, danhMuc);

    baiViet.setLuotXem(0L);
    baiViet.setHoatDong(false);

    // Chưa xuất bản thì chưa có ngày đăng
    baiViet.setNgayDang(null);

    BaiViet saved = baiVietRepository.save(baiViet);

    // Nếu admin chọn xuất bản ngay
    if (Boolean.TRUE.equals(dto.getHoatDong())) {
      kiemTraDuDieuKienXuatBan(saved);
      saved.setHoatDong(true);
      saved.setNgayDang(LocalDateTime.now());
    }

    return toResponseDTO(saved);
  }

  // =========================
  // CẬP NHẬT BÀI VIẾT
  // =========================

  @Transactional
  public BaiVietResponseDTO update(Long id, BaiVietRequestDTO dto) {

    BaiViet baiViet = baiVietRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy bài viết"));

    kiemTraSlugHopLe(dto.getDuongDanSeo(), id);

    DanhMucBaiViet danhMuc = layDanhMucHoatDong(dto.getDanhMucBaiVietId());

    boolean daXuatBan = Boolean.TRUE.equals(baiViet.getHoatDong());

    ganDuLieu(baiViet, dto, danhMuc);

    // Không cho phép thay đổi lượt xem và ngày tạo
    // Nếu bài viết đã xuất bản, giữ nguyên ngày đăng
    if (daXuatBan && Boolean.TRUE.equals(dto.getHoatDong())) {
      baiViet.setNgayDang(baiViet.getNgayDang());
      baiViet.setHoatDong(true);
    } else if (Boolean.TRUE.equals(dto.getHoatDong())) {
      kiemTraDuDieuKienXuatBan(baiViet);
      baiViet.setHoatDong(true);
      baiViet.setNgayDang(LocalDateTime.now());
    } else {
      baiViet.setHoatDong(false);
    }

    return toResponseDTO(baiVietRepository.save(baiViet));
  }

  // =========================
  // GÁN DỮ LIỆU
  // =========================

  private void ganDuLieu(
      BaiViet baiViet,
      BaiVietRequestDTO dto,
      DanhMucBaiViet danhMuc) {

    baiViet.setDanhMucBaiViet(danhMuc);

    baiViet.setTieuDe(dto.getTieuDe().trim());
    baiViet.setDuongDanSeo(dto.getDuongDanSeo().trim());

    baiViet.setTomTat(dto.getTomTat());
    baiViet.setNoiDung(dto.getNoiDung());

    baiViet.setHinhDaiDien(dto.getHinhDaiDien());
    baiViet.setHinhDaiDienAlt(dto.getHinhDaiDienAlt());

    baiViet.setTuKhoaChinh(dto.getTuKhoaChinh());
    baiViet.setTieuDeSeo(dto.getTieuDeSeo());
    baiViet.setMoTaSeo(dto.getMoTaSeo());
    baiViet.setTuKhoaSeo(dto.getTuKhoaSeo());

    baiViet.setNoiBat(Boolean.TRUE.equals(dto.getNoiBat()));
  }

  // =========================
  // KIỂM TRA ĐIỀU KIỆN XUẤT BẢN
  // =========================

  private void kiemTraDuDieuKienXuatBan(BaiViet baiViet) {

    if (baiViet.getTieuDe() == null || baiViet.getTieuDe().isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Bài viết cần có tiêu đề trước khi xuất bản");
    }

    if (baiViet.getDuongDanSeo() == null || baiViet.getDuongDanSeo().isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Bài viết cần có đường dẫn SEO trước khi xuất bản");
    }

    if (baiViet.getNoiDung() == null || baiViet.getNoiDung().isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Bài viết cần có nội dung trước khi xuất bản");
    }

    if (baiViet.getHinhDaiDien() == null || baiViet.getHinhDaiDien().isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Bài viết cần có ảnh đại diện trước khi xuất bản");
    }
  }

  // =========================
  // LẤY DANH MỤC HOẠT ĐỘNG
  // =========================

  private DanhMucBaiViet layDanhMucHoatDong(Long danhMucId) {

    DanhMucBaiViet danhMuc = danhMucRepository.findById(danhMucId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy danh mục bài viết"));

    if (!Boolean.TRUE.equals(danhMuc.getHoatDong())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Danh mục bài viết hiện không hoạt động");
    }

    return danhMuc;
  }

  // =========================
  // KIỂM TRA SLUG
  // =========================

  private void kiemTraSlugHopLe(String slug, Long id) {

    if (slug == null || !SLUG_PATTERN.matcher(slug).matches()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Slug không hợp lệ. Chỉ sử dụng chữ thường, số và dấu gạch ngang");
    }

    boolean daTonTai = id == null
        ? baiVietRepository.existsByDuongDanSeo(slug)
        : baiVietRepository.existsByDuongDanSeoAndIdNot(slug, id);

    if (daTonTai) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Đường dẫn SEO đã được sử dụng");
    }
  }

  @Transactional(readOnly = true)
  public KiemTraSlugBaiVietResponseDTO kiemTraSlug(String slug, Long id) {

    boolean hopLe = slug != null && SLUG_PATTERN.matcher(slug).matches();

    boolean daTonTai = false;

    if (hopLe) {
      daTonTai = id == null
          ? baiVietRepository.existsByDuongDanSeo(slug)
          : baiVietRepository.existsByDuongDanSeoAndIdNot(slug, id);
    }

    return new KiemTraSlugBaiVietResponseDTO(
        slug,
        daTonTai,
        hopLe && !daTonTai);
  }

  // =========================
  // CẬP NHẬT TRẠNG THÁI
  // =========================

  @Transactional
  public BaiVietResponseDTO capNhatTrangThai(
      Long id,
      CapNhatTrangThaiBaiVietDTO dto) {

    BaiViet baiViet = baiVietRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy bài viết"));

    if (Boolean.TRUE.equals(dto.getHoatDong())) {
      kiemTraDuDieuKienXuatBan(baiViet);

      if (!Boolean.TRUE.equals(baiViet.getHoatDong())) {
        baiViet.setNgayDang(LocalDateTime.now());
      }
    }

    baiViet.setHoatDong(dto.getHoatDong());

    return toResponseDTO(baiVietRepository.save(baiViet));
  }

  // =========================
  // XÓA MỀM
  // =========================

  @Transactional
  public void delete(Long id) {

    BaiViet baiViet = baiVietRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy bài viết"));

    // Không xóa dữ liệu vật lý, chỉ ngừng hoạt động
    baiViet.setHoatDong(false);

    baiVietRepository.save(baiViet);
  }

  // =========================
  // CÁC API CÔNG KHAI
  // =========================

  @Transactional(readOnly = true)
  public BaiVietResponseDTO getBySlug(String slug) {

    BaiViet baiViet = baiVietRepository
        .findByDuongDanSeoAndHoatDongTrue(slug)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy bài viết"));

    return toResponseDTO(baiViet);
  }

  @Transactional(readOnly = true)
  public Page<BaiVietResponseDTO> getByDanhMuc(
      Long danhMucId,
      Pageable pageable) {
    return baiVietRepository
        .findByDanhMucBaiViet_IdAndHoatDongTrueOrderByNgayDangDesc(
            danhMucId, pageable)
        .map(this::toResponseDTO);
  }

  @Transactional(readOnly = true)
  public Page<BaiVietResponseDTO> getNoiBat(Pageable pageable) {
    return baiVietRepository
        .findByHoatDongTrueAndNoiBatTrueOrderByNgayDangDesc(pageable)
        .map(this::toResponseDTO);
  }

  @Transactional(readOnly = true)
  public Page<BaiVietResponseDTO> getMoiNhat(Pageable pageable) {
    return baiVietRepository
        .findByHoatDongTrueOrderByNgayDangDesc(pageable)
        .map(this::toResponseDTO);
  }

  @Transactional(readOnly = true)
  public Page<BaiVietResponseDTO> timKiemTheoTieuDe(
      String keyword,
      Pageable pageable) {
    return baiVietRepository
        .findByTieuDeContainingIgnoreCaseAndHoatDongTrue(
            keyword, pageable)
        .map(this::toResponseDTO);
  }

  @Transactional(readOnly = true)
  public Page<BaiVietResponseDTO> getLienQuan(
      Long baiVietId,
      Pageable pageable) {

    BaiViet baiViet = baiVietRepository.findById(baiVietId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy bài viết"));

    if (!Boolean.TRUE.equals(baiViet.getHoatDong())) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy bài viết");
    }

    return baiVietRepository.findBaiVietLienQuan(
        baiViet.getDanhMucBaiViet().getId(),
        baiVietId,
        pageable).map(this::toResponseDTO);
  }

  @Transactional
  public void tangLuotXem(Long id) {

    int affected = baiVietRepository.tangLuotXem(id);

    if (affected == 0) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy bài viết hoặc bài viết chưa được xuất bản");
    }
  }

  // Danh sách theo trạng thái xuất bản
  @Transactional (readOnly = true)
  public Page<BaiVietResponseDTO> getTheoTrangThai(
      Boolean hoatDong,
      Pageable pageable) {
    return baiVietRepository
        .findByHoatDong(hoatDong, pageable)
        .map(this::toResponseDTO);
  }

}
