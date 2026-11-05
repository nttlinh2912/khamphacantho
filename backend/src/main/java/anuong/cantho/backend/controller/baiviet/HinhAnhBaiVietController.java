package anuong.cantho.backend.controller.baiviet;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import anuong.cantho.backend.dto.baiviet.hinhanhbaiviet.CapNhatThuTuHinhAnhDTO;
import anuong.cantho.backend.dto.baiviet.hinhanhbaiviet.HinhAnhBaiVietRequestDTO;
import anuong.cantho.backend.dto.baiviet.hinhanhbaiviet.HinhAnhBaiVietResponseDTO;
import anuong.cantho.backend.service.baiviet.HinhAnhBaiVietService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bai-viet/{baiVietId}/hinh-anh")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class HinhAnhBaiVietController {

  private final HinhAnhBaiVietService hinhAnhService;

  // ==========================================
  // 1. UPLOAD MỘT HOẶC NHIỀU ẢNH
  // ==========================================

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<List<HinhAnhBaiVietResponseDTO>> upload(

      @PathVariable Long baiVietId,

      @RequestParam("files") List<MultipartFile> files

  ) {

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(hinhAnhService.upload(baiVietId, files));
  }

  // ==========================================
  // 2. LẤY DANH SÁCH ẢNH CỦA BÀI VIẾT
  // ==========================================

  @GetMapping
  public ResponseEntity<List<HinhAnhBaiVietResponseDTO>> getByBaiViet(
      @PathVariable Long baiVietId) {

    return ResponseEntity.ok(
        hinhAnhService.getByBaiViet(baiVietId));
  }

  // ==========================================
  // 3. CẬP NHẬT ALT, CHÚ THÍCH, THỨ TỰ
  // ==========================================

  @PutMapping("/{hinhAnhId}")
  public ResponseEntity<HinhAnhBaiVietResponseDTO> capNhat(

      @PathVariable Long baiVietId,

      @PathVariable Long hinhAnhId,

      @Valid @RequestBody HinhAnhBaiVietRequestDTO dto

  ) {

    return ResponseEntity.ok(
        hinhAnhService.capNhat(baiVietId, hinhAnhId, dto));
  }

  // ==========================================
  // 4. CẬP NHẬT THỨ TỰ NHIỀU ẢNH
  // ==========================================

  @PutMapping("/thu-tu")
  public ResponseEntity<List<HinhAnhBaiVietResponseDTO>> capNhatThuTu(

      @PathVariable Long baiVietId,

      @Valid @RequestBody List<@Valid CapNhatThuTuHinhAnhDTO> danhSach

  ) {

    return ResponseEntity.ok(
        hinhAnhService.capNhatThuTu(baiVietId, danhSach));
  }

  // ==========================================
  // 5. XÓA ẢNH
  // ==========================================

  @DeleteMapping("/{hinhAnhId}")
  public ResponseEntity<Void> xoa(

      @PathVariable Long baiVietId,

      @PathVariable Long hinhAnhId

  ) {

    hinhAnhService.xoa(baiVietId, hinhAnhId);

    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{hinhAnhId}/anh-dai-dien")
  public ResponseEntity<Void> datLamAnhDaiDien(

      @PathVariable Long baiVietId,

      @PathVariable Long hinhAnhId

  ) {

    hinhAnhService.datLamAnhDaiDien(baiVietId, hinhAnhId);

    return ResponseEntity.noContent().build();
  }
}
