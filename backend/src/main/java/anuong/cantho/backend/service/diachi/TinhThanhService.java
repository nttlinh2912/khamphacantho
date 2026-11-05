package anuong.cantho.backend.service.diachi;

import java.util.List;

import anuong.cantho.backend.dto.diachi.TinhThanhRequestDTO;
import anuong.cantho.backend.dto.diachi.TinhThanhResponseDTO;

public interface TinhThanhService {

  List<TinhThanhResponseDTO> getAll();

  TinhThanhResponseDTO getById(Long id);

  TinhThanhResponseDTO create (TinhThanhRequestDTO request);

  TinhThanhResponseDTO update (Long id, TinhThanhRequestDTO request);

  void delete(Long id);

}
