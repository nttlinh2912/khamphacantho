package anuong.cantho.backend.service.diachi.impl.PhuongXa;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.diachi.PhuongXaRequestDTO;
import anuong.cantho.backend.dto.diachi.PhuongXaResponseDTO;
import anuong.cantho.backend.entity.diachi.PhuongXa;
import anuong.cantho.backend.entity.diachi.TinhThanh;
import anuong.cantho.backend.repository.diachi.PhuongXaRepository;
import anuong.cantho.backend.repository.diachi.TinhThanhRepository;
import anuong.cantho.backend.service.diachi.PhuongXaService;

@Service
@Transactional
public class PhuongXaServiceImpl implements PhuongXaService {

  private final PhuongXaRepository phuongXaRepository;
  private final TinhThanhRepository tinhThanhRepository;

  public PhuongXaServiceImpl(
      PhuongXaRepository phuongXaRepository,
      TinhThanhRepository tinhThanhRepository) {
    this.phuongXaRepository = phuongXaRepository;
    this.tinhThanhRepository = tinhThanhRepository;
  }

  // =====================================================
  // 1. LẤY TOÀN BỘ PHƯỜNG/XÃ
  // =====================================================
  @Override
  @Transactional(readOnly = true)
  public List<PhuongXaResponseDTO> getAll() {

    return phuongXaRepository.findAll(
        Sort.by(Sort.Direction.ASC, "tenPhuongXa"))
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  // =====================================================
  // 2. TÌM KIẾM, LỌC VÀ PHÂN TRANG
  // =====================================================
  @Override
  @Transactional(readOnly = true)
  public Page<PhuongXaResponseDTO> search(
      String keyword,
      Long tinhThanhId,
      Boolean hoatDong,
      int page,
      int size,
      String sortBy,
      String sortDir) {

    if (page < 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Số trang phải lớn hơn hoặc bằng 0");
    }

    if (size < 1 || size > 100) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Số bản ghi mỗi trang phải từ 1 đến 100");
    }

    if (tinhThanhId != null && tinhThanhId <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID tỉnh/thành không hợp lệ");
    }

    // Chỉ cho phép sắp xếp theo các thuộc tính được hỗ trợ
    List<String> allowedSortFields = List.of(
        "id",
        "tenPhuongXa",
        "maPhuongXa",
        "duongDanSeo",
        "hoatDong");

    if (sortBy == null || !allowedSortFields.contains(sortBy)) {
      sortBy = "tenPhuongXa";
    }

    Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
        ? Sort.Direction.DESC
        : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(direction, sortBy));

    String tuKhoa = chuanHoaTuKhoa(keyword);

    Page<PhuongXa> ketQua = phuongXaRepository.search(
        tuKhoa,
        tinhThanhId,
        hoatDong,
        pageable);

    return ketQua.map(this::toResponseDTO);
  }

  // =====================================================
  // 3. LẤY CHI TIẾT PHƯỜNG/XÃ
  // =====================================================
  @Override
  @Transactional(readOnly = true)
  public PhuongXaResponseDTO getById(Long id) {

    kiemTraId(id);

    PhuongXa phuongXa = phuongXaRepository
        .findByIdWithTinhThanh(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy phường/xã có ID: " + id));

    return toResponseDTO(phuongXa);
  }

  // =====================================================
  // 4. LẤY PHƯỜNG/XÃ THEO TỈNH/THÀNH
  // =====================================================
  @Override
  @Transactional(readOnly = true)
  public List<PhuongXaResponseDTO> getByTinhThanhId(Long tinhThanhId) {

    kiemTraId(tinhThanhId);

    // Kiểm tra tỉnh/thành có tồn tại không
    if (!tinhThanhRepository.existsById(tinhThanhId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy tỉnh/thành có ID: " + tinhThanhId);
    }

    return phuongXaRepository
        .findByTinhThanh_IdOrderByTenPhuongXaAsc(tinhThanhId)
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  // =====================================================
  // 5. THÊM PHƯỜNG/XÃ
  // =====================================================
  @Override
  public PhuongXaResponseDTO create(PhuongXaRequestDTO request) {

    if (request == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Dữ liệu gửi lên không được để trống");
    }

    String tenPhuongXa = chuanHoaBatBuoc(
        request.getTenPhuongXa(),
        "Tên phường/xã");

    String maPhuongXa = chuanHoaMa(
        request.getMaPhuongXa());

    // Tìm tỉnh/thành, nếu không tồn tại sẽ trả lỗi 404
    TinhThanh tinhThanh = tinhThanhRepository
        .findById(request.getTinhThanhId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Tỉnh/thành được chọn không tồn tại"));

    // Kiểm tra mã phường/xã bị trùng
    if (phuongXaRepository.existsByMaPhuongXaIgnoreCase(maPhuongXa)) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Mã phường/xã đã tồn tại");
    }

    // Nếu người dùng bỏ trống slug, tự tạo từ tên
    String duongDanSeo = chuanHoaSlug(
        request.getDuongDanSeo());

    if (duongDanSeo == null) {
      duongDanSeo = taoSlug(tenPhuongXa);
    }

    // Nếu slug tự sinh bị trùng thì tạo slug khác
    duongDanSeo = taoSlugKhongTrung(duongDanSeo);

    PhuongXa phuongXa = new PhuongXa();

    phuongXa.setTenPhuongXa(tenPhuongXa);
    phuongXa.setMaPhuongXa(maPhuongXa);
    phuongXa.setDuongDanSeo(duongDanSeo);
    phuongXa.setTinhThanh(tinhThanh);

    // Chỉ đặt mặc định true khi frontend không gửi giá trị
    phuongXa.setHoatDong(
        request.getHoatDong() == null
            ? true
            : request.getHoatDong());

    PhuongXa saved = phuongXaRepository.save(phuongXa);

    return toResponseDTO(saved);
  }

  // =====================================================
  // 6. CẬP NHẬT PHƯỜNG/XÃ
  // =====================================================
  @Override
  public PhuongXaResponseDTO update(
      Long id,
      PhuongXaRequestDTO request) {

    kiemTraId(id);

    if (request == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Dữ liệu gửi lên không được để trống");
    }

    PhuongXa phuongXa = phuongXaRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy phường/xã có ID: " + id));

    String tenPhuongXa = chuanHoaBatBuoc(
        request.getTenPhuongXa(),
        "Tên phường/xã");

    String maPhuongXa = chuanHoaMa(
        request.getMaPhuongXa());

    // Kiểm tra mã trùng, bỏ qua chính bản ghi đang sửa
    if (phuongXaRepository
        .existsByMaPhuongXaIgnoreCaseAndIdNot(maPhuongXa, id)) {

      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Mã phường/xã đã được sử dụng bởi bản ghi khác");
    }

    // Kiểm tra tỉnh/thành mới có tồn tại
    TinhThanh tinhThanh = tinhThanhRepository
        .findById(request.getTinhThanhId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Tỉnh/thành được chọn không tồn tại"));

    String duongDanSeo = chuanHoaSlug(
        request.getDuongDanSeo());

    if (duongDanSeo == null) {
      // Không truyền slug thì tạo lại theo tên mới
      duongDanSeo = taoSlug(tenPhuongXa);
    }

    // Không được trùng slug với bản ghi khác
    if (phuongXaRepository
        .existsByDuongDanSeoIgnoreCaseAndIdNot(duongDanSeo, id)) {

      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Đường dẫn SEO đã được sử dụng");
    }

    phuongXa.setTenPhuongXa(tenPhuongXa);
    phuongXa.setMaPhuongXa(maPhuongXa);
    phuongXa.setDuongDanSeo(duongDanSeo);
    phuongXa.setTinhThanh(tinhThanh);

    // Cho phép cập nhật trạng thái về false
    phuongXa.setHoatDong(
        request.getHoatDong() == null
            ? phuongXa.getHoatDong()
            : request.getHoatDong());

    PhuongXa saved = phuongXaRepository.save(phuongXa);

    return toResponseDTO(saved);
  }

  // =====================================================
  // 7. XÓA PHƯỜNG/XÃ
  // =====================================================
  @Override
  public void delete(Long id) {

    kiemTraId(id);

    PhuongXa phuongXa = phuongXaRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy phường/xã có ID: " + id));

    try {
      phuongXaRepository.delete(phuongXa);

      // Ép thực thi câu lệnh DELETE để bắt lỗi khóa ngoại tại đây
      phuongXaRepository.flush();

    } catch (DataIntegrityViolationException ex) {

      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Không thể xóa phường/xã này vì đang được dữ liệu khác sử dụng");
    }
  }

  // =====================================================
  // 8. CHUYỂN ENTITY SANG RESPONSE DTO
  // =====================================================
  private PhuongXaResponseDTO toResponseDTO(PhuongXa phuongXa) {

    PhuongXaResponseDTO dto = new PhuongXaResponseDTO();

    dto.setId(phuongXa.getId());
    dto.setTenPhuongXa(phuongXa.getTenPhuongXa());
    dto.setMaPhuongXa(phuongXa.getMaPhuongXa());
    dto.setDuongDanSeo(phuongXa.getDuongDanSeo());
    dto.setHoatDong(phuongXa.getHoatDong());

    if (phuongXa.getTinhThanh() != null) {
      dto.setTinhThanhId(phuongXa.getTinhThanh().getId());
      dto.setTenTinhThanh(
          phuongXa.getTinhThanh().getTenTinhThanh());
    }

    return dto;
  }

  // =====================================================
  // 9. KIỂM TRA ID
  // =====================================================
  private void kiemTraId(Long id) {

    if (id == null || id <= 0) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "ID phải là số nguyên dương");
    }
  }

  // =====================================================
  // 10. CHUẨN HÓA TÊN BẮT BUỘC
  // =====================================================
  private String chuanHoaBatBuoc(String value, String tenTruong) {

    if (value == null || value.trim().isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          tenTruong + " không được để trống");
    }

    return value.trim().replaceAll("\\s+", " ");
  }

  // =====================================================
  // 11. CHUẨN HÓA MÃ
  // =====================================================
  private String chuanHoaMa(String ma) {

    String ketQua = chuanHoaBatBuoc(ma, "Mã phường/xã");

    return ketQua.toUpperCase(Locale.ROOT);
  }

  // =====================================================
  // 12. CHUẨN HÓA TỪ KHÓA TÌM KIẾM
  // =====================================================
  private String chuanHoaTuKhoa(String keyword) {

    if (keyword == null || keyword.trim().isEmpty()) {
      return null;
    }

    return keyword.trim();
  }

  // =====================================================
  // 13. CHUẨN HÓA SLUG
  // =====================================================
  private String chuanHoaSlug(String slug) {

    if (slug == null || slug.trim().isEmpty()) {
      return null;
    }

    String ketQua = slug.trim().toLowerCase(Locale.ROOT);

    if (!ketQua.matches("[a-z0-9]+(?:-[a-z0-9]+)*")) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Đường dẫn SEO không hợp lệ");
    }

    return ketQua;
  }

  // =====================================================
  // 14. TẠO SLUG TỪ TÊN
  // =====================================================
  private String taoSlug(String ten) {

    String slug = ten.toLowerCase(Locale.ROOT);

    // Chuyển đ thành d trước khi loại bỏ dấu tiếng Việt
    slug = slug.replace('đ', 'd');

    slug = Normalizer.normalize(
        slug,
        Normalizer.Form.NFD);

    slug = Pattern.compile("\\p{M}+")
        .matcher(slug)
        .replaceAll("");

    slug = slug.replaceAll("[^a-z0-9]+", "-");
    slug = slug.replaceAll("(^-|-$)", "");

    if (slug.isBlank()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Không thể tạo đường dẫn SEO từ tên phường/xã");
    }

    return slug;
  }

  // =====================================================
  // 15. TẠO SLUG KHÔNG TRÙNG KHI THÊM MỚI
  // =====================================================
  private String taoSlugKhongTrung(String slugGoc) {

    String slug = slugGoc;
    int thuTu = 2;

    while (phuongXaRepository.existsByDuongDanSeoIgnoreCase(slug)) {
      slug = slugGoc + "-" + thuTu;
      thuTu++;
    }

    return slug;
  }
}
