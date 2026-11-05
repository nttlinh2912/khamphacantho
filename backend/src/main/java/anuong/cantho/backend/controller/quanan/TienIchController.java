package anuong.cantho.backend.controller.quanan;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.quanan.tienich.TienIchRequestDTO;
import anuong.cantho.backend.dto.quanan.tienich.TienIchResponseDTO;
import anuong.cantho.backend.service.quanan.TienIchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tien-ich")
// @CrossOrigin (origins = "http://localhost:5173")
@CrossOrigin(origins = "https://didaucantho.netlify.app")
@RequiredArgsConstructor
@Validated
public class TienIchController {
  private final TienIchService tienIchService;

  // GET /api/tien-ich
  @GetMapping
  public ResponseEntity<List<TienIchResponseDTO>> layTatCa() {
    return ResponseEntity.ok(tienIchService.layTatCa());
  }

  // GET /api/tien-ich/dang-hoat-dong
  @GetMapping("/dang-hoat-dong")
  public ResponseEntity<List<TienIchResponseDTO>> layDangHoatDong() {
    return ResponseEntity.ok(tienIchService.layDangHoatDong());
  }

  // GET /api/tien-ich/{id}
  @GetMapping("/{id}")
  public ResponseEntity<TienIchResponseDTO> layTheoId(
      @PathVariable Long id) {
    return ResponseEntity.ok(tienIchService.layTheoId(id));
  }

  // POST /api/tien-ich
  @PostMapping
  public ResponseEntity<TienIchResponseDTO> them(
      @Valid @RequestBody TienIchRequestDTO dto) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(tienIchService.them(dto));
  }

  // PUT /api/tien-ich/{id}
  @PutMapping("/{id}")
  public ResponseEntity<TienIchResponseDTO> capNhat(
      @PathVariable Long id,
      @Valid @RequestBody TienIchRequestDTO dto) {
    return ResponseEntity.ok(tienIchService.capNhat(id, dto));
  }

  // PATCH /api/tien-ich/{id}/trang-thai
  @PatchMapping("/{id}/trang-thai")
  public ResponseEntity<TienIchResponseDTO> doiTrangThai(
      @PathVariable Long id) {
    return ResponseEntity.ok(tienIchService.doiTrangThai(id));
  }

  // DELETE /api/tien-ich/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> xoa(@PathVariable Long id) {
    tienIchService.xoa(id);
    return ResponseEntity.noContent().build();
  }
}
