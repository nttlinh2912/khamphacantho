package anuong.cantho.backend.controller.quanan;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.quanan.quanandanhmuc.QuanAnDanhMucRequestDTO;
import anuong.cantho.backend.dto.quanan.quanandanhmuc.QuanAnDanhMucResponseDTO;
import anuong.cantho.backend.service.quanan.QuanAnDanhMucService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quan-an-danh-muc")
// @CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = "https://didaucantho.netlify.app")
@RequiredArgsConstructor
public class QuanAnDanhMucController {

  private final QuanAnDanhMucService service;

  // GET: Lấy tất cả liên kết
  @GetMapping
  public ResponseEntity<List<QuanAnDanhMucResponseDTO>> layTatCa() {
    return ResponseEntity.ok(service.layTatCa());
  }

  // GET: Lấy danh mục của một quán
  @GetMapping("/quan-an/{quanAnId}")
  public ResponseEntity<List<QuanAnDanhMucResponseDTO>> layTheoQuanAn(
      @PathVariable Long quanAnId) {
    return ResponseEntity.ok(
        service.layTheoQuanAn(quanAnId));
  }

  // GET: Lấy các quán thuộc một danh mục
  @GetMapping("/danh-muc/{danhMucId}")
  public ResponseEntity<List<QuanAnDanhMucResponseDTO>> layTheoDanhMuc(
      @PathVariable Long danhMucId) {
    return ResponseEntity.ok(
        service.layTheoDanhMuc(danhMucId));
  }

  // POST: Thêm liên kết
  @PostMapping
  public ResponseEntity<QuanAnDanhMucResponseDTO> them(
      @Valid @RequestBody QuanAnDanhMucRequestDTO request) {
    QuanAnDanhMucResponseDTO result = service.them(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(result);
  }

  // PUT: Cập nhật liên kết
  @PutMapping("/{id}")
  public ResponseEntity<QuanAnDanhMucResponseDTO> capNhat(
      @PathVariable Long id,
      @Valid @RequestBody QuanAnDanhMucRequestDTO request) {
    return ResponseEntity.ok(
        service.capNhat(id, request));
  }

  // DELETE: Xóa liên kết
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> xoa(
      @PathVariable Long id) {
    service.xoa(id);
    return ResponseEntity.noContent().build();
  }

}
