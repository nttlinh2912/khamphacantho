package anuong.cantho.backend.controller.quanan;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.quanan.giomocua.GioMoCuaRequestDTO;
import anuong.cantho.backend.dto.quanan.giomocua.GioMoCuaResponseDTO;
import anuong.cantho.backend.service.quanan.GioMoCuaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/gio-mo-cua")
@RequiredArgsConstructor
@Validated
// @CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = "https://didaucantho.netlify.app")
public class GioMoCuaController {

  private final GioMoCuaService gioMoCuaService;

  // GET: Lấy tất cả giờ mở cửa.
  @GetMapping
  public ResponseEntity<List<GioMoCuaResponseDTO>> layTatCa() {

    return ResponseEntity.ok(
        gioMoCuaService.layTatCa());
  }

  // GET: Lấy giờ mở cửa của một quán.
  @GetMapping("/quan-an/{quanAnId}")
  public ResponseEntity<List<GioMoCuaResponseDTO>> layTheoQuanAn(
      @PathVariable Long quanAnId) {

    return ResponseEntity.ok(
        gioMoCuaService.layTheoQuanAn(quanAnId));
  }

  // GET: Lấy một bản ghi theo ID.
  @GetMapping("/{id}")
  public ResponseEntity<GioMoCuaResponseDTO> layTheoId(
      @PathVariable Long id) {

    return ResponseEntity.ok(
        gioMoCuaService.layTheoId(id));
  }

  // GET: Lấy một bản ghi theo ID và ID quán.
  @GetMapping("/{id}/quan-an/{quanAnId}")
  public ResponseEntity<GioMoCuaResponseDTO> layTheoIdVaQuanAn(
      @PathVariable Long id,
      @PathVariable Long quanAnId) {

    return ResponseEntity.ok(
        gioMoCuaService.layTheoIdVaQuanAn(id, quanAnId));
  }

  // POST: Thêm giờ mở cửa.
  @PostMapping
  public ResponseEntity<GioMoCuaResponseDTO> them(
      @Valid @RequestBody GioMoCuaRequestDTO request) {

    GioMoCuaResponseDTO response = gioMoCuaService.them(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
  }

  // PUT: Cập nhật giờ mở cửa.
  @PutMapping("/{id}")
  public ResponseEntity<GioMoCuaResponseDTO> capNhat(
      @PathVariable Long id,
      @Valid @RequestBody GioMoCuaRequestDTO request) {

    return ResponseEntity.ok(
        gioMoCuaService.capNhat(id, request));
  }

  // DELETE: Xóa một bản ghi giờ mở cửa.
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> xoa(
      @PathVariable Long id) {

    gioMoCuaService.xoa(id);

    return ResponseEntity.noContent().build();
  }
}
