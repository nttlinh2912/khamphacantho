package anuong.cantho.backend.controller.baiviet;

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

import anuong.cantho.backend.dto.baiviet.baiviet.BaiVietRequestDTO;
import anuong.cantho.backend.dto.baiviet.baiviet.BaiVietResponseDTO;
import anuong.cantho.backend.dto.baiviet.baiviet.CapNhatTrangThaiBaiVietDTO;
import anuong.cantho.backend.dto.baiviet.baiviet.KiemTraSlugBaiVietResponseDTO;
import anuong.cantho.backend.service.baiviet.BaiVietService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bai-viet")
// @CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = "https://didaucantho.netlify.app")
@RequiredArgsConstructor
public class BaiVietController {

  private final BaiVietService baiVietService;

  // =========================
  // 1. DANH SÁCH BÀI VIẾT QUẢN TRỊ
  // =========================

  @GetMapping
  public ResponseEntity<Page<BaiVietResponseDTO>> getAll(

      @RequestParam(required = false) String keyword,

      @RequestParam(required = false) Long danhMucId,

      @RequestParam(required = false) Boolean hoatDong,

      @RequestParam(required = false) Boolean noiBat,

      @RequestParam(defaultValue = "0") int page,

      @RequestParam(defaultValue = "10") int size

  ) {

    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(Sort.Direction.DESC, "ngayTao"));

    return ResponseEntity.ok(
        baiVietService.getAll(
            keyword,
            danhMucId,
            hoatDong,
            noiBat,
            pageable));
  }

  // =========================
  // 2. LẤY CHI TIẾT BÀI VIẾT
  // =========================

  @GetMapping("/{id}")
  public ResponseEntity<BaiVietResponseDTO> getById(
      @PathVariable Long id) {
    return ResponseEntity.ok(
        baiVietService.getById(id));
  }

  // =========================
  // 3. THÊM BÀI VIẾT
  // =========================

  @PostMapping
  public ResponseEntity<BaiVietResponseDTO> create(
      @Valid @RequestBody BaiVietRequestDTO dto) {

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(baiVietService.create(dto));
  }

  // =========================
  // 4. CẬP NHẬT BÀI VIẾT
  // =========================

  @PutMapping("/{id}")
  public ResponseEntity<BaiVietResponseDTO> update(
      @PathVariable Long id,
      @Valid @RequestBody BaiVietRequestDTO dto) {

    return ResponseEntity.ok(
        baiVietService.update(id, dto));
  }

  // =========================
  // 5. CẬP NHẬT TRẠNG THÁI
  // =========================

  @PatchMapping("/{id}/trang-thai")
  public ResponseEntity<BaiVietResponseDTO> capNhatTrangThai(
      @PathVariable Long id,
      @Valid @RequestBody CapNhatTrangThaiBaiVietDTO dto) {

    return ResponseEntity.ok(
        baiVietService.capNhatTrangThai(id, dto));
  }

  // =========================
  // 6. XÓA MỀM BÀI VIẾT
  // =========================

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable Long id) {

    baiVietService.delete(id);

    return ResponseEntity.noContent().build();
  }

  // =========================
  // 7. KIỂM TRA SLUG
  // =========================

  @GetMapping("/kiem-tra-slug")
  public ResponseEntity<KiemTraSlugBaiVietResponseDTO> kiemTraSlug(

      @RequestParam String slug,

      @RequestParam(required = false) Long id

  ) {

    return ResponseEntity.ok(
        baiVietService.kiemTraSlug(slug, id));
  }

  // =========================
  // 8. LẤY BÀI VIẾT THEO SLUG
  // =========================

  @GetMapping("/slug/{slug}")
  public ResponseEntity<BaiVietResponseDTO> getBySlug(
      @PathVariable String slug) {

    return ResponseEntity.ok(
        baiVietService.getBySlug(slug));
  }

  // =========================
  // 9. LẤY BÀI VIẾT THEO DANH MỤC
  // =========================

  @GetMapping("/danh-muc/{danhMucId}")
  public ResponseEntity<Page<BaiVietResponseDTO>> getByDanhMuc(

      @PathVariable Long danhMucId,

      @RequestParam(defaultValue = "0") int page,

      @RequestParam(defaultValue = "10") int size

  ) {

    Pageable pageable = PageRequest.of(page, size);

    return ResponseEntity.ok(
        baiVietService.getByDanhMuc(danhMucId, pageable));
  }

  // =========================
  // 10. LẤY BÀI VIẾT NỔI BẬT
  // =========================

  @GetMapping("/noi-bat")
  public ResponseEntity<Page<BaiVietResponseDTO>> getNoiBat(

      @RequestParam(defaultValue = "0") int page,

      @RequestParam(defaultValue = "10") int size

  ) {

    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(Sort.Direction.DESC, "ngayDang"));

    return ResponseEntity.ok(
        baiVietService.getNoiBat(pageable));
  }

  // =========================
  // 11. LẤY BÀI VIẾT MỚI NHẤT
  // =========================

  @GetMapping("/moi-nhat")
  public ResponseEntity<Page<BaiVietResponseDTO>> getMoiNhat(

      @RequestParam(defaultValue = "0") int page,

      @RequestParam(defaultValue = "10") int size

  ) {

    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(Sort.Direction.DESC, "ngayDang"));

    return ResponseEntity.ok(
        baiVietService.getMoiNhat(pageable));
  }

  // =========================
  // 12. TÌM KIẾM THEO TIÊU ĐỀ
  // =========================

  @GetMapping("/tim-kiem")
  public ResponseEntity<Page<BaiVietResponseDTO>> timKiem(

      @RequestParam String keyword,

      @RequestParam(defaultValue = "0") int page,

      @RequestParam(defaultValue = "10") int size

  ) {

    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(Sort.Direction.DESC, "ngayDang"));

    return ResponseEntity.ok(
        baiVietService.timKiemTheoTieuDe(keyword, pageable));
  }

  // =========================
  // 13. LẤY BÀI VIẾT LIÊN QUAN
  // =========================

  @GetMapping("/{id}/lien-quan")
  public ResponseEntity<Page<BaiVietResponseDTO>> getLienQuan(

      @PathVariable Long id,

      @RequestParam(defaultValue = "5") int size

  ) {

    Pageable pageable = PageRequest.of(0, size);

    return ResponseEntity.ok(
        baiVietService.getLienQuan(id, pageable));
  }

  // =========================
  // 14. TĂNG LƯỢT XEM
  // =========================

  @PostMapping("/{id}/tang-luot-xem")
  public ResponseEntity<Void> tangLuotXem(
      @PathVariable Long id) {

    baiVietService.tangLuotXem(id);

    return ResponseEntity.noContent().build();
  }

  // =========================
  // 15. LẤY BÀI VIẾT THEO TRẠNG THÁI
  // =========================

  @GetMapping("/trang-thai/{hoatDong}")
  public ResponseEntity<Page<BaiVietResponseDTO>> getTheoTrangThai(

      @PathVariable Boolean hoatDong,

      @RequestParam(defaultValue = "0") int page,

      @RequestParam(defaultValue = "10") int size

  ) {

    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(Sort.Direction.DESC, "ngayTao"));

    return ResponseEntity.ok(
        baiVietService.getTheoTrangThai(hoatDong, pageable));
  }

}
