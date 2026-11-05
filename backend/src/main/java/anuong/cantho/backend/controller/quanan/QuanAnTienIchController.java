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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.quanan.tienich.QuanAnTienIchRequestDTO;
import anuong.cantho.backend.dto.quanan.tienich.QuanAnTienIchResponseDTO;
import anuong.cantho.backend.service.quanan.QuanAnTienIchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quan-an-tien-ich")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Validated
public class QuanAnTienIchController {

  private final QuanAnTienIchService quanAnTienIchService;

  // GET /api/quan-an-tien-ich/quan/1
  @GetMapping("/quan/{quanAnId}")
  public ResponseEntity<List<QuanAnTienIchResponseDTO>> layTheoQuanAn(
      @PathVariable Long quanAnId) {
    return ResponseEntity.ok(
        quanAnTienIchService.layTheoQuanAn(quanAnId));
  }

  // POST /api/quan-an-tien-ich
  @PostMapping
  public ResponseEntity<QuanAnTienIchResponseDTO> them(
      @Valid @RequestBody QuanAnTienIchRequestDTO dto) {
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(quanAnTienIchService.them(dto));
  }

  // DELETE /api/quan-an-tien-ich/quan/1/tien-ich/3
  @DeleteMapping("/quan/{quanAnId}/tien-ich/{tienIchId}")
  public ResponseEntity<Void> xoa(
      @PathVariable Long quanAnId,
      @PathVariable Long tienIchId) {
    quanAnTienIchService.xoa(quanAnId, tienIchId);
    return ResponseEntity.noContent().build();
  }

}
