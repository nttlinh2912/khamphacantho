package anuong.cantho.backend.service.quanan.Impl;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.quanan.danhmuc.CapNhatGiaTriDanhMucDTO;
import anuong.cantho.backend.dto.quanan.danhmuc.DanhMucRequestDTO;
import anuong.cantho.backend.dto.quanan.danhmuc.DanhMucResponseDTO;
import anuong.cantho.backend.entity.quanan.DanhMuc;
import anuong.cantho.backend.repository.quanan.DanhMucRepository;
import anuong.cantho.backend.repository.quanan.QuanAnDanhMucRepository;
import anuong.cantho.backend.service.quanan.DanhMucService;

@Service 
@Transactional (readOnly = true)
public class DanhMucServiceImpl implements DanhMucService{

  private final DanhMucRepository danhMucRepository;
    private final QuanAnDanhMucRepository quanAnDanhMucRepository;

    private static final Pattern SLUG_PATTERN =
        Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

    public DanhMucServiceImpl(
            DanhMucRepository danhMucRepository,
            QuanAnDanhMucRepository quanAnDanhMucRepository) {
        this.danhMucRepository = danhMucRepository;
        this.quanAnDanhMucRepository = quanAnDanhMucRepository;
    }

    // =====================================================
    // LẤY DANH SÁCH
    // =====================================================

    @Override
    public Page<DanhMucResponseDTO> getAll(Pageable pageable) {
        if (pageable == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Thông tin phân trang không hợp lệ"
            );
        }

        Pageable pageableSapXep = pageable.getSort().isSorted()
            ? pageable
            : org.springframework.data.domain.PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(
                    Sort.Order.asc("thuTuHienThi"),
                    Sort.Order.asc("id")
                )
            );

        return danhMucRepository.findAll(pageableSapXep)
            .map(this::toResponseDTO);
    }

    @Override
    public List<DanhMucResponseDTO> getAll() {
        return danhMucRepository.findAll(
                Sort.by(
                    Sort.Order.asc("thuTuHienThi"),
                    Sort.Order.asc("id")
                )
            )
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }

    // =====================================================
    // LẤY CHI TIẾT
    // =====================================================

    @Override
    public DanhMucResponseDTO getById(Long id) {
        return toResponseDTO(timDanhMucTheoId(id));
    }

    @Override
    public DanhMucResponseDTO getBySlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Đường dẫn SEO không được để trống"
            );
        }

        String slugDaChuanHoa = slug.trim().toLowerCase(Locale.ROOT);

        DanhMuc danhMuc = danhMucRepository
            .findByDuongDanSeo(slugDaChuanHoa)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Không tìm thấy danh mục với đường dẫn SEO: " + slug
            ));

        return toResponseDTO(danhMuc);
    }

    @Override
    public List<DanhMucResponseDTO> getDanhMucHoatDong() {
        return danhMucRepository
            .findByHoatDongTrueOrderByThuTuHienThiAscIdAsc()
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }

    @Override
    public List<DanhMucResponseDTO> getDanhMucNoiBat() {
        return danhMucRepository
            .findByNoiBatTrueAndHoatDongTrueOrderByThuTuHienThiAscIdAsc()
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }

    // =====================================================
    // TÌM KIẾM
    // =====================================================

    @Override
    public Page<DanhMucResponseDTO> timKiem(
            String keyword,
            Pageable pageable) {

        if (pageable == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Thông tin phân trang không hợp lệ"
            );
        }

        if (keyword == null || keyword.isBlank()) {
            return getAll(pageable);
        }

        String tuKhoa = keyword.trim();

        if (tuKhoa.length() > 255) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Từ khóa tìm kiếm không được vượt quá 255 ký tự"
            );
        }

        return danhMucRepository.timKiem(tuKhoa, pageable)
            .map(this::toResponseDTO);
    }

    // =====================================================
    // THÊM MỚI
    // =====================================================

    @Override
    @Transactional
    public DanhMucResponseDTO create(DanhMucRequestDTO request) {
        kiemTraRequest(request);

        DanhMuc danhMuc = new DanhMuc();

        ganDuLieu(danhMuc, request, null);

        try {
            DanhMuc daLuu = danhMucRepository.saveAndFlush(danhMuc);
            return toResponseDTO(daLuu);
        } catch (DataIntegrityViolationException ex) {
            throw xuLyLoiRangBuoc(ex);
        }
    }

    // =====================================================
    // CẬP NHẬT
    // =====================================================

    @Override
    @Transactional
    public DanhMucResponseDTO update(
            Long id,
            DanhMucRequestDTO request) {

        DanhMuc danhMuc = timDanhMucTheoId(id);

        kiemTraRequest(request);

        ganDuLieu(danhMuc, request, id);

        try {
            DanhMuc daLuu = danhMucRepository.saveAndFlush(danhMuc);
            return toResponseDTO(daLuu);
        } catch (DataIntegrityViolationException ex) {
            throw xuLyLoiRangBuoc(ex);
        }
    }

    // =====================================================
    // BẬT / TẮT HOẠT ĐỘNG
    // =====================================================

    @Override
    @Transactional
    public DanhMucResponseDTO capNhatTrangThai(
            Long id,
            CapNhatGiaTriDanhMucDTO request) {

        if (request == null || request.getGiaTri() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Giá trị trạng thái không được để trống"
            );
        }

        DanhMuc danhMuc = timDanhMucTheoId(id);

        danhMuc.setHoatDong(request.getGiaTri());

        try {
            return toResponseDTO(
                danhMucRepository.saveAndFlush(danhMuc)
            );
        } catch (DataIntegrityViolationException ex) {
            throw xuLyLoiRangBuoc(ex);
        }
    }

    // =====================================================
    // BẬT / TẮT NỔI BẬT
    // =====================================================

    @Override
    @Transactional
    public DanhMucResponseDTO capNhatNoiBat(
            Long id,
            CapNhatGiaTriDanhMucDTO request) {

        if (request == null || request.getGiaTri() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Giá trị nổi bật không được để trống"
            );
        }

        DanhMuc danhMuc = timDanhMucTheoId(id);

        danhMuc.setNoiBat(request.getGiaTri());

        try {
            return toResponseDTO(
                danhMucRepository.saveAndFlush(danhMuc)
            );
        } catch (DataIntegrityViolationException ex) {
            throw xuLyLoiRangBuoc(ex);
        }
    }

    // =====================================================
    // XÓA
    // =====================================================

    @Override
    @Transactional
    public void delete(Long id) {
        DanhMuc danhMuc = timDanhMucTheoId(id);

        // Kiểm tra khóa ngoại trước khi xóa
        boolean dangDuocSuDung =
            quanAnDanhMucRepository.existsByDanhMuc_Id(id);

        if (dangDuocSuDung) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Không thể xóa danh mục \"" +
                    danhMuc.getTenDanhMuc() +
                    "\" vì danh mục này đang được sử dụng bởi một hoặc nhiều quán ăn. " +
                    "Hãy gỡ danh mục khỏi các quán trước khi xóa, hoặc tắt trạng thái hoạt động."
            );
        }

        try {
            danhMucRepository.delete(danhMuc);
            danhMucRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            // Bảo vệ thêm trường hợp phát sinh liên kết trong lúc xóa
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Không thể xóa danh mục vì dữ liệu đang được liên kết. " +
                    "Hãy kiểm tra các bản ghi liên quan rồi thử lại.",
                ex
            );
        }
    }

    // =====================================================
    // KIỂM TRA ID
    // =====================================================

    private DanhMuc timDanhMucTheoId(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "ID danh mục phải là số nguyên dương"
            );
        }

        return danhMucRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Không tìm thấy danh mục có ID: " + id
            ));
    }

    // =====================================================
    // KIỂM TRA REQUEST
    // =====================================================

    private void kiemTraRequest(DanhMucRequestDTO request) {
        if (request == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Dữ liệu danh mục không được để trống"
            );
        }

        if (request.getTenDanhMuc() == null ||
                request.getTenDanhMuc().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Tên danh mục không được để trống"
            );
        }

        if (request.getTenDanhMuc().trim().length() > 255) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Tên danh mục tối đa 255 ký tự"
            );
        }

        if (request.getDuongDanSeo() != null &&
                request.getDuongDanSeo().trim().length() > 255) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Đường dẫn SEO tối đa 255 ký tự"
            );
        }

        if (request.getMoTa() != null &&
                request.getMoTa().length() > 1000) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Mô tả tối đa 1000 ký tự"
            );
        }

        if (request.getIcon() != null &&
                request.getIcon().trim().length() > 100) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Icon tối đa 100 ký tự"
            );
        }

        if (request.getThuTuHienThi() == null ||
                request.getThuTuHienThi() < 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Thứ tự hiển thị phải là số nguyên không âm"
            );
        }

        if (request.getNoiBat() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Trạng thái nổi bật không được để trống"
            );
        }

        if (request.getHoatDong() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Trạng thái hoạt động không được để trống"
            );
        }
    }

    // =====================================================
    // GÁN DỮ LIỆU VÀ KIỂM TRA SLUG
    // =====================================================

    private void ganDuLieu(
            DanhMuc danhMuc,
            DanhMucRequestDTO request,
            Long idDangCapNhat) {

        String ten = request.getTenDanhMuc().trim();

        String slugNhapVao = request.getDuongDanSeo();

        String slug = slugNhapVao == null || slugNhapVao.isBlank()
            ? taoSlug(ten)
            : slugNhapVao.trim().toLowerCase(Locale.ROOT);

        if (!SLUG_PATTERN.matcher(slug).matches()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Đường dẫn SEO chỉ được chứa chữ thường không dấu, chữ số và dấu gạch ngang; " +
                    "không được có dấu gạch ngang ở đầu hoặc cuối"
            );
        }

        if (slug.length() > 255) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Đường dẫn SEO tối đa 255 ký tự"
            );
        }

        boolean slugDaTonTai = idDangCapNhat == null
            ? danhMucRepository.existsByDuongDanSeo(slug)
            : danhMucRepository.existsByDuongDanSeoAndIdNot(
                slug,
                idDangCapNhat
            );

        if (slugDaTonTai) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Đường dẫn SEO \"" + slug + "\" đã được sử dụng. " +
                    "Vui lòng chọn đường dẫn khác."
            );
        }

        danhMuc.setTenDanhMuc(ten);
        danhMuc.setDuongDanSeo(slug);
        danhMuc.setMoTa(chuanHoaChuoi(request.getMoTa()));
        danhMuc.setIcon(chuanHoaChuoi(request.getIcon()));
        danhMuc.setThuTuHienThi(request.getThuTuHienThi());
        danhMuc.setNoiBat(request.getNoiBat());
        danhMuc.setHoatDong(request.getHoatDong());
    }

    private String chuanHoaChuoi(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty() ? null : trimmed;
    }

    // =====================================================
    // TẠO SLUG TIẾNG VIỆT
    // =====================================================

    private String taoSlug(String value) {
        String slug = value.trim().toLowerCase(Locale.ROOT);

        // Chuẩn hóa Unicode và loại bỏ dấu tiếng Việt
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{M}+", "");

        // Chuyển đ thành d
        slug = slug.replace('đ', 'd');

        // Các nhóm ký tự không phải chữ/số thành dấu gạch ngang
        slug = slug.replaceAll("[^a-z0-9]+", "-");

        // Loại bỏ dấu gạch ngang ở đầu và cuối
        slug = slug.replaceAll("^-+|-+$", "");

        if (slug.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Không thể tạo đường dẫn SEO từ tên danh mục này"
            );
        }

        return slug;
    }

    // =====================================================
    // CHUYỂN ENTITY THÀNH DTO
    // =====================================================

    private DanhMucResponseDTO toResponseDTO(DanhMuc danhMuc) {
        return new DanhMucResponseDTO(
            danhMuc.getId(),
            danhMuc.getTenDanhMuc(),
            danhMuc.getDuongDanSeo(),
            danhMuc.getMoTa(),
            danhMuc.getIcon(),
            danhMuc.getThuTuHienThi(),
            danhMuc.getNoiBat(),
            danhMuc.getHoatDong()
        );
    }

    // =====================================================
    // CHUYỂN LỖI RÀNG BUỘC DATABASE THÀNH LỖI HTTP
    // =====================================================

    private ResponseStatusException xuLyLoiRangBuoc(
            DataIntegrityViolationException ex) {

        return new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Dữ liệu danh mục bị trùng hoặc vi phạm ràng buộc database. " +
                "Hãy kiểm tra đường dẫn SEO và các dữ liệu liên quan.",
            ex
        );
    }

}
