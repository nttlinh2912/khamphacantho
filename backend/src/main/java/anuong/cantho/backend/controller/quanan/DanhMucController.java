package anuong.cantho.backend.controller.quanan;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.quanan.danhmuc.CapNhatGiaTriDanhMucDTO;
import anuong.cantho.backend.dto.quanan.danhmuc.DanhMucRequestDTO;
import anuong.cantho.backend.dto.quanan.danhmuc.DanhMucResponseDTO;
import anuong.cantho.backend.service.quanan.DanhMucService;

@RestController 
@RequestMapping ("/api/danh-muc")
// @CrossOrigin (origins = "http://localhost:5173")
@CrossOrigin(origins = "https://didaucantho.netlify.app")
public class DanhMucController {

  private final DanhMucService danhMucService;

    public DanhMucController(DanhMucService danhMucService) {
        this.danhMucService = danhMucService;
    }

    // =====================================================
    // LẤY DANH SÁCH CÓ PHÂN TRANG
    // =====================================================

    @GetMapping
    public ResponseEntity<Page<DanhMucResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = taoPageable(page, size);

        return ResponseEntity.ok(
            danhMucService.getAll(pageable)
        );
    }

    // =====================================================
    // LẤY TOÀN BỘ DANH MỤC
    // =====================================================

    @GetMapping("/all")
    public ResponseEntity<List<DanhMucResponseDTO>> getAll() {
        return ResponseEntity.ok(
            danhMucService.getAll()
        );
    }

    // =====================================================
    // DANH MỤC ĐANG HOẠT ĐỘNG
    // =====================================================

    @GetMapping("/hoat-dong")
    public ResponseEntity<List<DanhMucResponseDTO>> getDanhMucHoatDong() {
        return ResponseEntity.ok(
            danhMucService.getDanhMucHoatDong()
        );
    }

    // =====================================================
    // DANH MỤC NỔI BẬT
    // =====================================================

    @GetMapping("/noi-bat")
    public ResponseEntity<List<DanhMucResponseDTO>> getDanhMucNoiBat() {
        return ResponseEntity.ok(
            danhMucService.getDanhMucNoiBat()
        );
    }

    // =====================================================
    // TÌM KIẾM
    // =====================================================

    @GetMapping("/tim-kiem")
    public ResponseEntity<Page<DanhMucResponseDTO>> timKiem(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = taoPageable(page, size);

        return ResponseEntity.ok(
            danhMucService.timKiem(keyword, pageable)
        );
    }

    // =====================================================
    // CHI TIẾT THEO SLUG
    // =====================================================

    @GetMapping("/slug/{slug}")
    public ResponseEntity<DanhMucResponseDTO> getBySlug(
            @PathVariable String slug) {

        return ResponseEntity.ok(
            danhMucService.getBySlug(slug)
        );
    }

    // =====================================================
    // CHI TIẾT THEO ID
    // =====================================================

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<DanhMucResponseDTO> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
            danhMucService.getById(id)
        );
    }

    // =====================================================
    // THÊM MỚI
    // =====================================================

    @PostMapping
    public ResponseEntity<DanhMucResponseDTO> create(
            @Valid @RequestBody DanhMucRequestDTO request) {

        DanhMucResponseDTO result = danhMucService.create(request);

        return ResponseEntity
            .created(URI.create("/api/danh-muc/" + result.getId()))
            .body(result);
    }

    // =====================================================
    // CẬP NHẬT
    // =====================================================

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<DanhMucResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DanhMucRequestDTO request) {

        return ResponseEntity.ok(
            danhMucService.update(id, request)
        );
    }

    // =====================================================
    // CẬP NHẬT TRẠNG THÁI
    // =====================================================

    @PatchMapping("/{id:\\d+}/trang-thai")
    public ResponseEntity<DanhMucResponseDTO> capNhatTrangThai(
            @PathVariable Long id,
            @Valid @RequestBody CapNhatGiaTriDanhMucDTO request) {

        return ResponseEntity.ok(
            danhMucService.capNhatTrangThai(id, request)
        );
    }

    // =====================================================
    // CẬP NHẬT NỔI BẬT
    // =====================================================

    @PatchMapping("/{id:\\d+}/noi-bat")
    public ResponseEntity<DanhMucResponseDTO> capNhatNoiBat(
            @PathVariable Long id,
            @Valid @RequestBody CapNhatGiaTriDanhMucDTO request) {

        return ResponseEntity.ok(
            danhMucService.capNhatNoiBat(id, request)
        );
    }

    // =====================================================
    // XÓA
    // =====================================================

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        danhMucService.delete(id);

        return ResponseEntity.noContent().build();
    }

    // =====================================================
    // VALIDATE PHÂN TRANG
    // =====================================================

    private Pageable taoPageable(int page, int size) {
        if (page < 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Số trang không được âm"
            );
        }

        if (size < 1 || size > 100) {
            throw new org.springframework.web.server.ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Kích thước trang phải nằm trong khoảng 1 đến 100"
            );
        }

        return PageRequest.of(
            page,
            size,
            Sort.by(
                Sort.Order.asc("thuTuHienThi"),
                Sort.Order.asc("id")
            )
        );
    }

}
