package anuong.cantho.backend.service.quanan;

import java.util.List;

import anuong.cantho.backend.dto.quanan.giomocua.GioMoCuaRequestDTO;
import anuong.cantho.backend.dto.quanan.giomocua.GioMoCuaResponseDTO;

public interface GioMoCuaService {

  List<GioMoCuaResponseDTO> layTatCa();

  List<GioMoCuaResponseDTO> layTheoQuanAn(Long quanAnId);

  GioMoCuaResponseDTO layTheoId(Long id);

  GioMoCuaResponseDTO them(GioMoCuaRequestDTO request);

  GioMoCuaResponseDTO capNhat(
      Long id,
      GioMoCuaRequestDTO request);

  void xoa(Long id);

  GioMoCuaResponseDTO layTheoIdVaQuanAn(
      Long id,
      Long quanAnId);
}
