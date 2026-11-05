package anuong.cantho.backend.service.diachi.impl.TinhThanh;

import java.util.List;

import org.springframework.stereotype.Service;

import anuong.cantho.backend.dto.diachi.TinhThanhRequestDTO;
import anuong.cantho.backend.dto.diachi.TinhThanhResponseDTO;
import anuong.cantho.backend.entity.diachi.TinhThanh;
import anuong.cantho.backend.repository.diachi.TinhThanhRepository;
import anuong.cantho.backend.service.diachi.TinhThanhService;

@Service

public class TinhThanhServiceImpl implements TinhThanhService {

  private final TinhThanhRepository tinhThanhRepository;

  public TinhThanhServiceImpl(TinhThanhRepository tinhThanhRepository) {
    this.tinhThanhRepository = tinhThanhRepository;
  }

  private TinhThanhResponseDTO toDTO(TinhThanh tinhThanh) {
    return TinhThanhResponseDTO.builder()
        .id(tinhThanh.getId())
        .tenTinhThanh(tinhThanh.getTenTinhThanh())
        .maTinhThanh(tinhThanh.getMaTinhThanh())
        .duongDanSeo(tinhThanh.getDuongDanSeo())
        .hoatDong(tinhThanh.getHoatDong())
        .build();
  }

  @Override
  public List<TinhThanhResponseDTO> getAll() {
    return tinhThanhRepository.findAll()
        .stream()
        .map(this::toDTO)
        .toList();
  }

  @Override
  public TinhThanhResponseDTO getById(Long id) {
    TinhThanh tinhThanh = tinhThanhRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy tỉnh thành"));

    return toDTO(tinhThanh);
  }

  @Override
  public TinhThanhResponseDTO create(TinhThanhRequestDTO request) {

    if (tinhThanhRepository.existsByMaTinhThanh(request.getMaTinhThanh())) {
      throw new RuntimeException("Mã tỉnh thành đã tồn tại");
    }

    TinhThanh tinhThanh = new TinhThanh();

    tinhThanh.setTenTinhThanh(request.getTenTinhThanh());
    tinhThanh.setMaTinhThanh(request.getMaTinhThanh());
    tinhThanh.setDuongDanSeo(request.getDuongDanSeo());
    tinhThanh.setHoatDong(request.getHoatDong());

    return toDTO(tinhThanhRepository.save(tinhThanh));
  }

  @Override
  public TinhThanhResponseDTO update(Long id, TinhThanhRequestDTO request) {

    TinhThanh tinhThanh = tinhThanhRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy tỉnh thành"));

    tinhThanh.setTenTinhThanh(request.getTenTinhThanh());
    tinhThanh.setMaTinhThanh(request.getMaTinhThanh());
    tinhThanh.setDuongDanSeo(request.getDuongDanSeo());
    tinhThanh.setHoatDong(request.getHoatDong());

    return toDTO(tinhThanhRepository.save(tinhThanh));
  }

  @Override
  public void delete(Long id) {

    TinhThanh tinhThanh = tinhThanhRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy tỉnh thành"));

    tinhThanhRepository.delete(tinhThanh);
  }

}
