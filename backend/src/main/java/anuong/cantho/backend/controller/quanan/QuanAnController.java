package anuong.cantho.backend.controller.quanan;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import anuong.cantho.backend.dto.quanan.quan.CapNhatTrangThaiQuanAnDTO;
import anuong.cantho.backend.dto.quanan.quan.QuanAnRequestDTO;
import anuong.cantho.backend.dto.quanan.quan.QuanAnResponseDTO;
import anuong.cantho.backend.service.quanan.QuanAnService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quan-an")
@Validated
@CrossOrigin(origins = "http://localhost:5173")
public class QuanAnController {

  private final QuanAnService quanAnService;

  public QuanAnController(QuanAnService quanAnService) {
    this.quanAnService = quanAnService;
  }

  // =====================================================
  // 1. LẤY DANH SÁCH, TÌM KIẾM VÀ LỌC
  // =====================================================

  @GetMapping
  public ResponseEntity<Page<QuanAnResponseDTO>> getAll(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Long tinhThanhId,
      @RequestParam(required = false) Long phuongXaId,
      @RequestParam(required = false) Boolean hoatDong,
      @RequestParam(required = false) Boolean noiBat,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = taoPageable(page, size);

    return ResponseEntity.ok(
        quanAnService.getAll(
            keyword,
            tinhThanhId,
            phuongXaId,
            hoatDong,
            noiBat,
            pageable));
  }

  // =====================================================
  // 2. LẤY QUÁN ĐANG HOẠT ĐỘNG
  // =====================================================

  @GetMapping("/hoat-dong")
  public ResponseEntity<Page<QuanAnResponseDTO>> getQuanAnHoatDong(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = taoPageable(page, size);

    return ResponseEntity.ok(
        quanAnService.getQuanAnHoatDong(pageable));
  }

  // =====================================================
  // 3. LẤY CHI TIẾT THEO SLUG
  // =====================================================

  @GetMapping("/slug/{slug}")
  public ResponseEntity<QuanAnResponseDTO> getBySlug(
      @PathVariable String slug) {
    return ResponseEntity.ok(
        quanAnService.getBySlug(slug));
  }

  // =====================================================
  // 4. LẤY CHI TIẾT THEO ID
  // =====================================================

  @GetMapping("/{id}")
  public ResponseEntity<QuanAnResponseDTO> getById(
      @PathVariable Long id) {
    return ResponseEntity.ok(
        quanAnService.getById(id));
  }

  // =====================================================
  // 5. THÊM QUÁN ĂN
  // =====================================================

  @PostMapping
  public ResponseEntity<QuanAnResponseDTO> create(
      @Valid @RequestBody QuanAnRequestDTO request) {
    QuanAnResponseDTO result = quanAnService.create(request);

    return ResponseEntity
        .created(URI.create("/api/quan-an/" + result.getId()))
        .body(result);
  }

  // =====================================================
  // 6. SỬA QUÁN ĂN
  // =====================================================

  @PutMapping("/{id}")
  public ResponseEntity<QuanAnResponseDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody QuanAnRequestDTO request) {
    return ResponseEntity.ok(
        quanAnService.update(id, request));
  }

  // =====================================================
  // 7. BẬT / TẮT HOẠT ĐỘNG
  // =====================================================

  @PatchMapping("/{id}/trang-thai")
  public ResponseEntity<QuanAnResponseDTO> capNhatTrangThai(
      @PathVariable Long id,
      @Valid @RequestBody CapNhatTrangThaiQuanAnDTO request) {
    return ResponseEntity.ok(
        quanAnService.capNhatTrangThai(id, request));
  }

  // =====================================================
  // 8. BẬT / TẮT NỔI BẬT
  // =====================================================

  @PatchMapping("/{id}/noi-bat")
  public ResponseEntity<QuanAnResponseDTO> capNhatNoiBat(
      @PathVariable Long id,
      @Valid @RequestBody CapNhatTrangThaiQuanAnDTO request) {
    return ResponseEntity.ok(
        quanAnService.capNhatNoiBat(id, request));
  }

  // =====================================================
  // 9. XÓA MỀM
  // =====================================================

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> xoaMem(
      @PathVariable Long id) {
    quanAnService.xoaMem(id);

    return ResponseEntity.noContent().build();
  }

  // =====================================================
  // 10. KHÔI PHỤC
  // =====================================================

  @PostMapping("/{id}/khoi-phuc")
  public ResponseEntity<QuanAnResponseDTO> khoiPhuc(
      @PathVariable Long id) {
    return ResponseEntity.ok(
        quanAnService.khoiPhuc(id));
  }

  // =====================================================
  // 11. PHÂN TRANG VÀ SẮP XẾP
  // =====================================================

  private Pageable taoPageable(int page, int size) {
    if (page < 0) {
      throw new org.springframework.web.server.ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Số trang không được âm");
    }

    if (size < 1 || size > 100) {
      throw new org.springframework.web.server.ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Kích thước trang phải từ 1 đến 100");
    }

    return PageRequest.of(
        page,
        size,
        Sort.by(
            Sort.Order.desc("ngayTao"),
            Sort.Order.desc("id")));
  }
}
