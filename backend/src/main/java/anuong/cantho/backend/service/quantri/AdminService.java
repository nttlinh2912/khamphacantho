package anuong.cantho.backend.service.quantri;

import java.util.List;

import anuong.cantho.backend.dto.quantri.AdminRequestDTO;
import anuong.cantho.backend.dto.quantri.AdminResponseDTO;

public interface AdminService {

  List<AdminResponseDTO> findAll();

  AdminResponseDTO findById(Long id);

  AdminResponseDTO create(AdminRequestDTO dto);

  AdminResponseDTO update(Long id, AdminRequestDTO dto);

  void delete(Long id);

  AdminResponseDTO login(String tenDangNhap, String matKhau);

}
