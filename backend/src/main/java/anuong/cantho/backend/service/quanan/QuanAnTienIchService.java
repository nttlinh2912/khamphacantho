package anuong.cantho.backend.service.quanan;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.quanan.tienich.QuanAnTienIchRequestDTO;
import anuong.cantho.backend.dto.quanan.tienich.QuanAnTienIchResponseDTO;
import anuong.cantho.backend.entity.quanan.QuanAn;
import anuong.cantho.backend.entity.quanan.QuanAnTienIch;
import anuong.cantho.backend.entity.quanan.TienIch;
import anuong.cantho.backend.repository.quanan.QuanAnRepository;
import anuong.cantho.backend.repository.quanan.QuanAnTienIchRepository;
import anuong.cantho.backend.repository.quanan.TienIchRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class QuanAnTienIchService {

  private final QuanAnTienIchRepository quanAnTienIchRepository;

  private final QuanAnRepository quanAnRepository;

  private final TienIchRepository tienIchRepository;

  // Lấy tiện ích của một quán
  @Transactional(readOnly = true)
  public List<QuanAnTienIchResponseDTO> layTheoQuanAn(Long quanAnId) {

    if (!quanAnRepository.existsById(quanAnId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Không tìm thấy quán ăn có ID: " + quanAnId);
    }

    return quanAnTienIchRepository
        .findByQuanAn_IdOrderByTienIch_TenTienIchAsc(quanAnId)
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  // Gán một tiện ích cho một quán
  public QuanAnTienIchResponseDTO them(
      QuanAnTienIchRequestDTO dto) {
    Long quanAnId = dto.getQuanAnId();
    Long tienIchId = dto.getTienIchId();

    QuanAn quanAn = quanAnRepository.findById(quanAnId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy quán ăn có ID: " + quanAnId));

    TienIch tienIch = tienIchRepository.findById(tienIchId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy tiện ích có ID: " + tienIchId));

    if (!Boolean.TRUE.equals(tienIch.getHoatDong())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Không thể gán tiện ích đã ngừng hoạt động");
    }

    if (quanAnTienIchRepository.existsByQuanAn_IdAndTienIch_Id(
        quanAnId, tienIchId)) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Quán đã được gán tiện ích này");
    }

    QuanAnTienIch quanAnTienIch = new QuanAnTienIch();
    quanAnTienIch.setQuanAn(quanAn);
    quanAnTienIch.setTienIch(tienIch);

    return toResponseDTO(
        quanAnTienIchRepository.save(quanAnTienIch));
  }

  // Bỏ một tiện ích khỏi quán
  public void xoa(Long quanAnId, Long tienIchId) {

    QuanAnTienIch quanAnTienIch = quanAnTienIchRepository
        .findByQuanAn_IdAndTienIch_Id(quanAnId, tienIchId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Quán chưa được gán tiện ích này"));

    quanAnTienIchRepository.delete(quanAnTienIch);
  }

  // Chuyển entity sang DTO
  private QuanAnTienIchResponseDTO toResponseDTO(
      QuanAnTienIch quanAnTienIch) {
    QuanAnTienIchResponseDTO dto = new QuanAnTienIchResponseDTO();

    TienIch tienIch = quanAnTienIch.getTienIch();

    dto.setId(quanAnTienIch.getId());
    dto.setQuanAnId(quanAnTienIch.getQuanAn().getId());
    dto.setTienIchId(tienIch.getId());
    dto.setTenTienIch(tienIch.getTenTienIch());
    dto.setIcon(tienIch.getIcon());
    dto.setHoatDong(tienIch.getHoatDong());

    return dto;
  }
}
