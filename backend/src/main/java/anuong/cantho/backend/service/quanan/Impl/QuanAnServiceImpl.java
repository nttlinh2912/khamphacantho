package anuong.cantho.backend.service.quanan.Impl;

import java.text.Normalizer;
import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.quanan.quan.CapNhatTrangThaiQuanAnDTO;
import anuong.cantho.backend.dto.quanan.quan.QuanAnRequestDTO;
import anuong.cantho.backend.dto.quanan.quan.QuanAnResponseDTO;
import anuong.cantho.backend.entity.diachi.PhuongXa;
import anuong.cantho.backend.entity.diachi.TinhThanh;
import anuong.cantho.backend.entity.quanan.QuanAn;
import anuong.cantho.backend.repository.diachi.PhuongXaRepository;
import anuong.cantho.backend.repository.diachi.TinhThanhRepository;
import anuong.cantho.backend.repository.quanan.GioMoCuaRepository;
import anuong.cantho.backend.repository.quanan.HinhAnhQuanRepository;
import anuong.cantho.backend.repository.quanan.QuanAnDanhMucRepository;
import anuong.cantho.backend.repository.quanan.QuanAnRepository;
import anuong.cantho.backend.repository.quanan.QuanAnTienIchRepository;
import anuong.cantho.backend.service.quanan.QuanAnService;

@Service
@Transactional(readOnly = true)
public class QuanAnServiceImpl implements QuanAnService {

  private final QuanAnRepository quanAnRepository;
  private final TinhThanhRepository tinhThanhRepository;
  private final PhuongXaRepository phuongXaRepository;
  private final GioMoCuaRepository gioMoCuaRepository;
  private final HinhAnhQuanRepository hinhAnhQuanRepository;
  private final QuanAnDanhMucRepository quanAnDanhMucRepository;
  private final QuanAnTienIchRepository quanAnTienIchRepository;

  public QuanAnServiceImpl(
      QuanAnRepository quanAnRepository,
      TinhThanhRepository tinhThanhRepository,
      PhuongXaRepository phuongXaRepository,
      GioMoCuaRepository gioMoCuaRepository,
      HinhAnhQuanRepository hinhAnhQuanRepository,
      QuanAnDanhMucRepository quanAnDanhMucRepository,
      QuanAnTienIchRepository quanAnTienIchRepository) {
    this.quanAnRepository = quanAnRepository;
    this.tinhThanhRepository = tinhThanhRepository;
    this.phuongXaRepository = phuongXaRepository;
    this.gioMoCuaRepository = gioMoCuaRepository;
    this.hinhAnhQuanRepository = hinhAnhQuanRepository;
    this.quanAnDanhMucRepository = quanAnDanhMucRepository;
    this.quanAnTienIchRepository = quanAnTienIchRepository;
  }

  // =====================================================
  // 1. LẤY DANH SÁCH, TÌM KIẾM VÀ LỌC
  // =====================================================

  @Override
  public Page<QuanAnResponseDTO> getAll(
      String keyword,
      Long tinhThanhId,
      Long phuongXaId,
      Boolean hoatDong,
      Boolean noiBat,
      Pageable pageable) {
    kiemTraIdLoc(tinhThanhId, "Tỉnh/thành");
    kiemTraIdLoc(phuongXaId, "Phường/xã");

    if (tinhThanhId != null
        && !tinhThanhRepository.existsById(tinhThanhId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy tỉnh/thành có ID " + tinhThanhId);
    }

    if (phuongXaId != null) {
      PhuongXa phuongXa = phuongXaRepository.findById(phuongXaId)
          .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "Không tìm thấy phường/xã có ID " + phuongXaId));

      if (tinhThanhId != null
          && !tinhThanhId.equals(
              phuongXa.getTinhThanh().getId())) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Phường/xã không thuộc tỉnh/thành đã chọn");
      }
    }

    String tuKhoa = chuanHoa(keyword);

    return quanAnRepository.timKiemVaLoc(
        tuKhoa,
        tinhThanhId,
        phuongXaId,
        hoatDong,
        noiBat,
        pageable)
        .map(this::toResponseDTO);
  }

  @Override
  public Page<QuanAnResponseDTO> getQuanAnHoatDong(
      Pageable pageable) {
    return quanAnRepository
        .findByHoatDongTrue(pageable)
        .map(this::toResponseDTO);
  }

  // =====================================================
  // 2. LẤY CHI TIẾT
  // =====================================================
  private void ganThongKeLienQuan(
      QuanAnResponseDTO dto,
      Long quanAnId) {
    dto.setSoGioMoCua(
        gioMoCuaRepository.countByQuanAn_Id(quanAnId));

    dto.setSoHinhAnh(
        hinhAnhQuanRepository.countByQuanAn_Id(quanAnId));

    dto.setSoDanhMuc(
        quanAnDanhMucRepository.countByQuanAn_Id(quanAnId));

    dto.setSoTienIch(
        quanAnTienIchRepository.countByQuanAn_Id(quanAnId));
  }

  @Override
  public QuanAnResponseDTO getById(Long id) {
    kiemTraId(id);

    QuanAn quanAn = timQuanAn(id);

    QuanAnResponseDTO dto = toResponseDTO(quanAn);

    ganThongKeLienQuan(dto, quanAn.getId());

    return dto;
  }

  @Override
  public QuanAnResponseDTO getBySlug(String slug) {
    if (slug == null || slug.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Đường dẫn SEO không được để trống");
    }

    QuanAn quanAn = quanAnRepository
        .findByDuongDanSeo(slug.trim())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy quán ăn"));

    QuanAnResponseDTO dto = toResponseDTO(quanAn);

    ganThongKeLienQuan(dto, quanAn.getId());

    return dto;
  }

  // =====================================================
  // 3. THÊM QUÁN ĂN
  // =====================================================

  @Override
  @Transactional
  public QuanAnResponseDTO create(QuanAnRequestDTO request) {
    if (request == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Dữ liệu quán ăn không được để trống");
    }

    kiemTraDuLieu(request);

    QuanAn quanAn = new QuanAn();

    ganDuLieu(quanAn, request);

    try {
      QuanAn daLuu = quanAnRepository.saveAndFlush(quanAn);

      return toResponseDTO(daLuu);

    } catch (DataIntegrityViolationException ex) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Không thể thêm quán. Đường dẫn SEO có thể đã tồn tại "
              + "hoặc dữ liệu vi phạm ràng buộc database.",
          ex);
    }
  }

  // =====================================================
  // 4. CẬP NHẬT QUÁN ĂN
  // =====================================================

  @Override
  @Transactional
  public QuanAnResponseDTO update(
      Long id,
      QuanAnRequestDTO request) {
    kiemTraId(id);

    if (request == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Dữ liệu cập nhật không được để trống");
    }

    kiemTraDuLieu(request);

    QuanAn quanAn = timQuanAn(id);

    ganDuLieu(quanAn, request);

    try {
      QuanAn daLuu = quanAnRepository.saveAndFlush(quanAn);

      return toResponseDTO(daLuu);

    } catch (DataIntegrityViolationException ex) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Không thể cập nhật quán. Đường dẫn SEO có thể đã "
              + "được sử dụng hoặc dữ liệu vi phạm ràng buộc.",
          ex);
    }
  }

  // =====================================================
  // 5. BẬT / TẮT HOẠT ĐỘNG
  // =====================================================

  @Override
  @Transactional
  public QuanAnResponseDTO capNhatTrangThai(
      Long id,
      CapNhatTrangThaiQuanAnDTO request) {
    kiemTraId(id);
    kiemTraGiaTri(request);

    QuanAn quanAn = timQuanAn(id);

    boolean dangHoatDong = request.getGiaTri();

    quanAn.setHoatDong(dangHoatDong);

    // Quán bị tắt hoạt động thì không còn được đánh dấu nổi bật.
    if (!dangHoatDong) {
      quanAn.setNoiBat(false);
    }

    QuanAn daLuu = quanAnRepository.saveAndFlush(quanAn);

    return toResponseDTO(daLuu);
  }

  // =====================================================
  // 6. BẬT / TẮT NỔI BẬT
  // =====================================================

  @Override
  @Transactional
  public QuanAnResponseDTO capNhatNoiBat(
      Long id,
      CapNhatTrangThaiQuanAnDTO request) {
    kiemTraId(id);
    kiemTraGiaTri(request);

    QuanAn quanAn = timQuanAn(id);

    if (!Boolean.TRUE.equals(quanAn.getHoatDong())
        && Boolean.TRUE.equals(request.getGiaTri())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Không thể đánh dấu nổi bật cho quán đã tắt hoạt động");
    }

    quanAn.setNoiBat(request.getGiaTri());

    QuanAn daLuu = quanAnRepository.saveAndFlush(quanAn);

    return toResponseDTO(daLuu);
  }

  // =====================================================
  // 7. XÓA MỀM QUÁN ĂN
  // =====================================================

  @Override
  @Transactional
  public void xoaMem(Long id) {
    kiemTraId(id);

    QuanAn quanAn = timQuanAn(id);

    // Không xóa bản ghi khỏi database.
    // Giữ nguyên các quan hệ với 4 bảng liên quan.
    quanAn.setHoatDong(false);
    quanAn.setNoiBat(false);

    quanAnRepository.saveAndFlush(quanAn);
  }

  // =====================================================
  // 8. KHÔI PHỤC QUÁN ĂN
  // =====================================================

  @Override
  @Transactional
  public QuanAnResponseDTO khoiPhuc(Long id) {
    kiemTraId(id);

    QuanAn quanAn = timQuanAn(id);

    // Chỉ bật hoạt động lại; không tự bật nổi bật.
    quanAn.setHoatDong(true);

    QuanAn daLuu = quanAnRepository.saveAndFlush(quanAn);

    return toResponseDTO(daLuu);
  }

  // =====================================================
  // 9. KIỂM TRA DỮ LIỆU
  // =====================================================

  private void kiemTraDuLieu(QuanAnRequestDTO request) {
    if (request.getTenQuan() == null
        || request.getTenQuan().isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Tên quán không được để trống");
    }

    if (request.getTinhThanhId() == null
        || request.getPhuongXaId() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng chọn tỉnh/thành và phường/xã");
    }

    if (request.getGiaTu() != null
        && request.getGiaDen() != null
        && request.getGiaTu().compareTo(request.getGiaDen()) > 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Giá từ không được lớn hơn giá đến");
    }

    boolean coViDo = request.getViDo() != null;
    boolean coKinhDo = request.getKinhDo() != null;

    if (coViDo != coKinhDo) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng nhập cả vĩ độ và kinh độ, hoặc để trống cả hai");
    }

    if (request.getNoiBat() == null
        || request.getHoatDong() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Vui lòng chọn trạng thái hoạt động và nổi bật");
    }
  }

  // =====================================================
  // 10. GÁN DỮ LIỆU VÀO ENTITY
  // =====================================================

  private void ganDuLieu(
      QuanAn quanAn,
      QuanAnRequestDTO request) {
    TinhThanh tinhThanh = tinhThanhRepository
        .findById(request.getTinhThanhId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy tỉnh/thành có ID "
                + request.getTinhThanhId()));

    PhuongXa phuongXa = phuongXaRepository
        .findById(request.getPhuongXaId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy phường/xã có ID "
                + request.getPhuongXaId()));

    // Quan trọng: phường/xã phải thuộc tỉnh/thành đã chọn.
    if (!tinhThanh.getId().equals(
        phuongXa.getTinhThanh().getId())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Phường/xã đã chọn không thuộc tỉnh/thành này");
    }

    String slugNhap = chuanHoa(request.getDuongDanSeo());

    String slug = slugNhap == null
        ? taoSlug(request.getTenQuan())
        : taoSlug(slugNhap);

    if (slug.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Không thể tạo đường dẫn SEO từ tên quán");
    }

    boolean trungSlug = quanAn.getId() == null
        ? quanAnRepository.existsByDuongDanSeo(slug)
        : quanAnRepository.existsByDuongDanSeoAndIdNot(
            slug,
            quanAn.getId());

    if (trungSlug) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Đường dẫn SEO đã được sử dụng: " + slug);
    }

    quanAn.setTenQuan(request.getTenQuan().trim());
    quanAn.setDuongDanSeo(slug);
    quanAn.setDiaChi(chuanHoa(request.getDiaChi()));

    quanAn.setTinhThanh(tinhThanh);
    quanAn.setPhuongXa(phuongXa);

    quanAn.setSoDienThoai(chuanHoa(request.getSoDienThoai()));
    quanAn.setEmail(chuanHoa(request.getEmail()));

    quanAn.setWebsite(chuanHoa(request.getWebsite()));
    quanAn.setUrlFacebook(chuanHoa(request.getUrlFacebook()));
    quanAn.setUrlTiktok(chuanHoa(request.getUrlTiktok()));
    quanAn.setUrlGoogleMap(chuanHoa(request.getUrlGoogleMap()));

    quanAn.setViDo(request.getViDo());
    quanAn.setKinhDo(request.getKinhDo());

    quanAn.setMoTaNgan(chuanHoa(request.getMoTaNgan()));
    quanAn.setMoTaChiTiet(chuanHoa(request.getMoTaChiTiet()));

    quanAn.setGiaTu(request.getGiaTu());
    quanAn.setGiaDen(request.getGiaDen());

    quanAn.setDiemDanhGiaGoogle(request.getDiemDanhGiaGoogle());
    quanAn.setTongDanhGiaGoogle(request.getTongDanhGiaGoogle());

    quanAn.setNoiBat(request.getNoiBat());
    quanAn.setHoatDong(request.getHoatDong());

    quanAn.setTieuDeSeo(chuanHoa(request.getTieuDeSeo()));
    quanAn.setMoTaSeo(chuanHoa(request.getMoTaSeo()));
    quanAn.setTuKhoaSeo(chuanHoa(request.getTuKhoaSeo()));
  }

  // =====================================================
  // 11. CHUYỂN ENTITY THÀNH RESPONSE DTO
  // =====================================================

  private QuanAnResponseDTO toResponseDTO(QuanAn quanAn) {
    QuanAnResponseDTO dto = new QuanAnResponseDTO();

    dto.setId(quanAn.getId());

    dto.setTenQuan(quanAn.getTenQuan());
    dto.setDuongDanSeo(quanAn.getDuongDanSeo());
    dto.setDiaChi(quanAn.getDiaChi());

    if (quanAn.getTinhThanh() != null) {
      dto.setTinhThanhId(quanAn.getTinhThanh().getId());
      dto.setTenTinhThanh(quanAn.getTinhThanh().getTenTinhThanh());
    }

    if (quanAn.getPhuongXa() != null) {
      dto.setPhuongXaId(quanAn.getPhuongXa().getId());
      dto.setTenPhuongXa(quanAn.getPhuongXa().getTenPhuongXa());
    }

    dto.setSoDienThoai(quanAn.getSoDienThoai());
    dto.setEmail(quanAn.getEmail());

    dto.setWebsite(quanAn.getWebsite());
    dto.setUrlFacebook(quanAn.getUrlFacebook());
    dto.setUrlTiktok(quanAn.getUrlTiktok());
    dto.setUrlGoogleMap(quanAn.getUrlGoogleMap());

    dto.setViDo(quanAn.getViDo());
    dto.setKinhDo(quanAn.getKinhDo());

    dto.setMoTaNgan(quanAn.getMoTaNgan());
    dto.setMoTaChiTiet(quanAn.getMoTaChiTiet());

    dto.setGiaTu(quanAn.getGiaTu());
    dto.setGiaDen(quanAn.getGiaDen());

    dto.setDiemDanhGiaGoogle(quanAn.getDiemDanhGiaGoogle());
    dto.setTongDanhGiaGoogle(quanAn.getTongDanhGiaGoogle());
    dto.setLuotXem(quanAn.getLuotXem());

    dto.setNoiBat(quanAn.getNoiBat());
    dto.setHoatDong(quanAn.getHoatDong());

    dto.setTieuDeSeo(quanAn.getTieuDeSeo());
    dto.setMoTaSeo(quanAn.getMoTaSeo());
    dto.setTuKhoaSeo(quanAn.getTuKhoaSeo());

    dto.setNgayTao(quanAn.getNgayTao());
    dto.setNgayCapNhat(quanAn.getNgayCapNhat());

    return dto;
  }

  // =====================================================
  // 12. HÀM HỖ TRỢ
  // =====================================================

  private QuanAn timQuanAn(Long id) {
    return quanAnRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy quán ăn có ID " + id));
  }

  private void kiemTraId(Long id) {
    if (id == null || id <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID quán ăn không hợp lệ");
    }
  }

  private void kiemTraIdLoc(Long id, String tenTruong) {
    if (id != null && id <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          tenTruong + " không hợp lệ");
    }
  }

  private void kiemTraGiaTri(
      CapNhatTrangThaiQuanAnDTO request) {
    if (request == null || request.getGiaTri() == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Giá trị cập nhật không được để trống");
    }
  }

  private String chuanHoa(String value) {
    if (value == null) {
      return null;
    }

    String result = value.trim();

    return result.isEmpty() ? null : result;
  }

  private String taoSlug(String value) {
    if (value == null || value.isBlank()) {
      return "";
    }

    String slug = Normalizer.normalize(
        value.trim(),
        Normalizer.Form.NFD);

    slug = slug.replaceAll("\\p{M}+", "");
    slug = slug.replace('đ', 'd').replace('Đ', 'D');
    slug = slug.toLowerCase(Locale.ROOT);
    slug = slug.replaceAll("[^a-z0-9]+", "-");
    slug = slug.replaceAll("^-+|-+$", "");

    return slug;
  }

}
