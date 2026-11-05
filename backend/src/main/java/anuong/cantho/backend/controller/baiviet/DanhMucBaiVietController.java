package anuong.cantho.backend.controller.baiviet;

import java.util.List;
import java.util.Map;

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

import anuong.cantho.backend.dto.baiviet.danhmucbaiviet.CapNhatThuTuDanhMucDTO;
import anuong.cantho.backend.dto.baiviet.danhmucbaiviet.CapNhatTrangThaiDanhMucDTO;
import anuong.cantho.backend.dto.baiviet.danhmucbaiviet.DanhMucBaiVietRequestDTO;
import anuong.cantho.backend.dto.baiviet.danhmucbaiviet.DanhMucBaiVietResponseDTO;
import anuong.cantho.backend.service.baiviet.DanhMucBaiVietService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/danh-muc-bai-viet")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@Validated
public class DanhMucBaiVietController {

  private final DanhMucBaiVietService danhMucBaiVietService;

  // =====================================================
  // 1. LẤY TẤT CẢ DANH MỤC
  // GET /api/danh-muc-bai-viet
  // =====================================================

  @GetMapping
  public ResponseEntity<List<DanhMucBaiVietResponseDTO>> layTatCa() {

    return ResponseEntity.ok(
        danhMucBaiVietService.layTatCa());
  }

  // =====================================================
  // 2. LẤY DANH MỤC ĐANG HOẠT ĐỘNG
  // GET /api/danh-muc-bai-viet/hoat-dong
  // =====================================================

  @GetMapping("/hoat-dong")
  public ResponseEntity<List<DanhMucBaiVietResponseDTO>> layDanhMucHoatDong() {

    return ResponseEntity.ok(
        danhMucBaiVietService.layDanhMucHoatDong());
  }

  // =====================================================
  // 3. KIỂM TRA SLUG
  // GET /api/danh-muc-bai-viet/kiem-tra-slug?slug=...
  // =====================================================

  @GetMapping("/kiem-tra-slug")
  public ResponseEntity<Map<String, Object>> kiemTraSlug(
      @RequestParam String slug,
      @RequestParam(required = false) Long id) {

    boolean daTonTai = danhMucBaiVietService.kiemTraSlug(
        slug,
        id);

    return ResponseEntity.ok(
        Map.of(
            "slug", slug.trim(),
            "daTonTai", daTonTai,
            "hopLe", !daTonTai));
  }

  // =====================================================
  // 4. LẤY DANH MỤC THEO SLUG
  // GET /api/danh-muc-bai-viet/slug/{slug}
  // =====================================================

  @GetMapping("/slug/{slug}")
  public ResponseEntity<DanhMucBaiVietResponseDTO> layTheoSlug(
      @PathVariable String slug) {

    return ResponseEntity.ok(
        danhMucBaiVietService.layTheoSlug(slug));
  }

  // =====================================================
  // 5. LẤY CHI TIẾT THEO ID
  // GET /api/danh-muc-bai-viet/{id}
  // =====================================================

  @GetMapping("/{id}")
  public ResponseEntity<DanhMucBaiVietResponseDTO> layTheoId(
      @PathVariable Long id) {

    return ResponseEntity.ok(
        danhMucBaiVietService.layTheoId(id));
  }

  // =====================================================
  // 6. THÊM DANH MỤC
  // POST /api/danh-muc-bai-viet
  // =====================================================

  @PostMapping
  public ResponseEntity<DanhMucBaiVietResponseDTO> them(
      @Valid @RequestBody DanhMucBaiVietRequestDTO request) {

    DanhMucBaiVietResponseDTO response = danhMucBaiVietService.them(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
  }

  // =====================================================
  // 7. CẬP NHẬT DANH MỤC
  // PUT /api/danh-muc-bai-viet/{id}
  // =====================================================

  @PutMapping("/{id}")
  public ResponseEntity<DanhMucBaiVietResponseDTO> capNhat(
      @PathVariable Long id,
      @Valid @RequestBody DanhMucBaiVietRequestDTO request) {

    return ResponseEntity.ok(
        danhMucBaiVietService.capNhat(id, request));
  }

  // =====================================================
  // 8. CẬP NHẬT TRẠNG THÁI
  // PATCH /api/danh-muc-bai-viet/{id}/trang-thai
  // =====================================================

  @PatchMapping("/{id}/trang-thai")
  public ResponseEntity<DanhMucBaiVietResponseDTO> capNhatTrangThai(
      @PathVariable Long id,
      @Valid @RequestBody CapNhatTrangThaiDanhMucDTO request) {

    return ResponseEntity.ok(
        danhMucBaiVietService.capNhatTrangThai(
            id,
            request.hoatDong()));
  }

  // =====================================================
  // 9. CẬP NHẬT THỨ TỰ
  // PATCH /api/danh-muc-bai-viet/cap-nhat-thu-tu
  // =====================================================

  @PatchMapping("/cap-nhat-thu-tu")
  public ResponseEntity<List<DanhMucBaiVietResponseDTO>> capNhatThuTu(
      @Valid @RequestBody CapNhatThuTuDanhMucDTO request) {

    return ResponseEntity.ok(
        danhMucBaiVietService.capNhatThuTu(request));
  }

  // =====================================================
  // 10. XÓA DANH MỤC
  // DELETE /api/danh-muc-bai-viet/{id}
  // =====================================================

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> xoa(
      @PathVariable Long id) {

    danhMucBaiVietService.xoa(id);

    return ResponseEntity.ok(
        Map.of(
            "message",
            "Xóa danh mục bài viết thành công"));
  }

}
