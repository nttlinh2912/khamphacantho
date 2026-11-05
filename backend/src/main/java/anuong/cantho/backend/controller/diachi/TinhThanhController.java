package anuong.cantho.backend.controller.diachi;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.diachi.TinhThanhRequestDTO;
import anuong.cantho.backend.dto.diachi.TinhThanhResponseDTO;
import anuong.cantho.backend.service.diachi.TinhThanhService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tinh-thanh")
@CrossOrigin(origins = "http://localhost:5173")
public class TinhThanhController {

  private final TinhThanhService tinhThanhService;

  public TinhThanhController(TinhThanhService tinhThanhService){
    this.tinhThanhService = tinhThanhService;
  }

  @GetMapping
  public List<TinhThanhResponseDTO> getAll() {
    return tinhThanhService.getAll();
  }

  @GetMapping("/{id}")
  public TinhThanhResponseDTO getById(@PathVariable Long id) {
    return tinhThanhService.getById(id);
  }

  @PostMapping
  public TinhThanhResponseDTO create(
      @Valid @RequestBody TinhThanhRequestDTO request) {
    return tinhThanhService.create(request);
  }

  @PutMapping("/{id}")
  public TinhThanhResponseDTO update(
      @PathVariable Long id,
      @Valid @RequestBody TinhThanhRequestDTO request) {
    return tinhThanhService.update(id, request);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    tinhThanhService.delete(id);
  }

}
