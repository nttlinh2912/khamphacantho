package anuong.cantho.backend.service.baiviet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.baiviet.danhmucbaiviet.CapNhatThuTuDanhMucDTO;
import anuong.cantho.backend.dto.baiviet.danhmucbaiviet.DanhMucBaiVietRequestDTO;
import anuong.cantho.backend.dto.baiviet.danhmucbaiviet.DanhMucBaiVietResponseDTO;
import anuong.cantho.backend.entity.baiviet.DanhMucBaiViet;
import anuong.cantho.backend.repository.baiviet.BaiVietRepository;
import anuong.cantho.backend.repository.baiviet.DanhMucBaiVietRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DanhMucBaiVietService {
  private final DanhMucBaiVietRepository danhMucRepository;
  private final BaiVietRepository baiVietRepository;

  // =====================================================
  // 1. LẤY TẤT CẢ DANH MỤC
  // =====================================================

  @Transactional(readOnly = true)
  public List<DanhMucBaiVietResponseDTO> layTatCa() {

    return danhMucRepository
        .findAllByOrderByThuTuAscIdAsc()
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  // =====================================================
  // 2. LẤY DANH MỤC ĐANG HOẠT ĐỘNG
  // =====================================================

  @Transactional(readOnly = true)
  public List<DanhMucBaiVietResponseDTO> layDanhMucHoatDong() {

    return danhMucRepository
        .findByHoatDongTrueOrderByThuTuAscIdAsc()
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  // =====================================================
  // 3. LẤY CHI TIẾT THEO ID
  // =====================================================

  @Transactional(readOnly = true)
  public DanhMucBaiVietResponseDTO layTheoId(Long id) {

    DanhMucBaiViet danhMuc = timTheoId(id);

    return toResponseDTO(danhMuc);
  }

  // =====================================================
  // 4. LẤY DANH MỤC THEO SLUG
  // =====================================================

  @Transactional(readOnly = true)
  public DanhMucBaiVietResponseDTO layTheoSlug(String slug) {

    if (slug == null || slug.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Đường dẫn SEO không được để trống");
    }

    DanhMucBaiViet danhMuc = danhMucRepository
        .findByDuongDanSeo(slug.trim())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy danh mục bài viết"));

    return toResponseDTO(danhMuc);
  }

  // =====================================================
  // 5. KIỂM TRA SLUG ĐÃ TỒN TẠI HAY CHƯA
  // =====================================================

  @Transactional(readOnly = true)
  public boolean kiemTraSlug(
      String slug,
      Long idLoaiTru) {

    if (slug == null || slug.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Đường dẫn SEO không được để trống");
    }

    String slugChuanHoa = slug.trim();

    // Khi cập nhật, bỏ qua chính danh mục đang sửa
    if (idLoaiTru != null) {
      if (!danhMucRepository.existsById(idLoaiTru)) {
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy danh mục bài viết");
      }

      return danhMucRepository
          .existsByDuongDanSeoAndIdNot(
              slugChuanHoa,
              idLoaiTru);
    }

    return danhMucRepository
        .existsByDuongDanSeo(slugChuanHoa);
  }

  // =====================================================
  // 6. THÊM DANH MỤC
  // =====================================================

  @Transactional 
  public DanhMucBaiVietResponseDTO them(
      DanhMucBaiVietRequestDTO request) {

    String slug = request.duongDanSeo().trim();

    if (danhMucRepository.existsByDuongDanSeo(slug)) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Đường dẫn SEO đã tồn tại");
    }

    DanhMucBaiViet danhMuc = new DanhMucBaiViet();

    danhMuc.setTenDanhMuc(request.tenDanhMuc().trim());
    danhMuc.setDuongDanSeo(slug);
    danhMuc.setMoTa(chuanHoaMoTa(request.moTa()));

    danhMuc.setThuTu(
        request.thuTu() != null
            ? request.thuTu()
            : 0);

    danhMuc.setHoatDong(
        request.hoatDong() != null
            ? request.hoatDong()
            : true);

    try {
      DanhMucBaiViet daLuu = danhMucRepository.saveAndFlush(danhMuc);

      return toResponseDTO(daLuu);

    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Không thể thêm danh mục. Đường dẫn SEO có thể đã tồn tại.",
          e);
    }
  }

  // =====================================================
  // 7. CẬP NHẬT DANH MỤC
  // =====================================================

  @Transactional
  public DanhMucBaiVietResponseDTO capNhat(
      Long id,
      DanhMucBaiVietRequestDTO request) {

    DanhMucBaiViet danhMuc = timTheoId(id);

    String slug = request.duongDanSeo().trim();

    if (danhMucRepository.existsByDuongDanSeoAndIdNot(
        slug,
        id)) {

      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Đường dẫn SEO đã được sử dụng bởi danh mục khác");
    }

    danhMuc.setTenDanhMuc(request.tenDanhMuc().trim());
    danhMuc.setDuongDanSeo(slug);
    danhMuc.setMoTa(chuanHoaMoTa(request.moTa()));

    if (request.thuTu() != null) {
      danhMuc.setThuTu(request.thuTu());
    }

    if (request.hoatDong() != null) {
      danhMuc.setHoatDong(request.hoatDong());
    }

    try {
      DanhMucBaiViet daLuu = danhMucRepository.saveAndFlush(danhMuc);

      return toResponseDTO(daLuu);

    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Không thể cập nhật danh mục. Đường dẫn SEO có thể đã tồn tại.",
          e);
    }
  }

  // =====================================================
  // 8. CẬP NHẬT TRẠNG THÁI HOẠT ĐỘNG
  // =====================================================

  @Transactional
  public DanhMucBaiVietResponseDTO capNhatTrangThai(
      Long id,
      Boolean hoatDong) {

    DanhMucBaiViet danhMuc = timTheoId(id);

    danhMuc.setHoatDong(hoatDong);

    DanhMucBaiViet daLuu = danhMucRepository.save(danhMuc);

    return toResponseDTO(daLuu);
  }

  // =====================================================
  // 9. CẬP NHẬT THỨ TỰ NHIỀU DANH MỤC
  // =====================================================

  @Transactional
  public List<DanhMucBaiVietResponseDTO> capNhatThuTu(
      CapNhatThuTuDanhMucDTO request) {

    Set<Long> ids = new HashSet<>();

    // Kiểm tra ID trùng và ID không tồn tại trước khi cập nhật
    for (CapNhatThuTuDanhMucDTO.ChiTietThuTu item : request.danhSach()) {

      if (!ids.add(item.id())) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Danh sách có ID danh mục bị trùng: " + item.id());
      }

      if (!danhMucRepository.existsById(item.id())) {
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy danh mục có ID: " + item.id());
      }
    }

    List<DanhMucBaiViet> danhSachDaCapNhat = new ArrayList<>();

    for (CapNhatThuTuDanhMucDTO.ChiTietThuTu item : request.danhSach()) {

      DanhMucBaiViet danhMuc = timTheoId(item.id());

      danhMuc.setThuTu(item.thuTu());

      danhSachDaCapNhat.add(danhMuc);
    }

    danhMucRepository.saveAll(danhSachDaCapNhat);

    return danhSachDaCapNhat.stream()
        .sorted(
            java.util.Comparator
                .comparing(DanhMucBaiViet::getThuTu)
                .thenComparing(DanhMucBaiViet::getId))
        .map(this::toResponseDTO)
        .toList();
  }

  // =====================================================
  // 10. XÓA DANH MỤC
  // =====================================================

  @Transactional
  public void xoa(Long id) {

    DanhMucBaiViet danhMuc = timTheoId(id);

    boolean coBaiViet = baiVietRepository
        .existsByDanhMucBaiViet_Id(id);

    if (coBaiViet) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Danh mục đang có bài viết liên quan. "
              + "Vui lòng chuyển các bài viết sang danh mục khác "
              + "trước khi xóa.");
    }

    danhMucRepository.delete(danhMuc);
  }

  // =====================================================
  // 11. HÀM HỖ TRỢ
  // =====================================================

  private DanhMucBaiViet timTheoId(Long id) {

    if (id == null || id <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID danh mục không hợp lệ");
    }

    return danhMucRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy danh mục bài viết có ID: " + id));
  }

  private String chuanHoaMoTa(String moTa) {

    if (moTa == null || moTa.isBlank()) {
      return null;
    }

    return moTa.trim();
  }

  private DanhMucBaiVietResponseDTO toResponseDTO(
      DanhMucBaiViet danhMuc) {

    return new DanhMucBaiVietResponseDTO(
        danhMuc.getId(),
        danhMuc.getTenDanhMuc(),
        danhMuc.getDuongDanSeo(),
        danhMuc.getMoTa(),
        danhMuc.getThuTu(),
        danhMuc.getHoatDong());
  }
}
