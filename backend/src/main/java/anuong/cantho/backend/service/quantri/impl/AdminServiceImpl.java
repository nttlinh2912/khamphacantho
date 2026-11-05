
package anuong.cantho.backend.service.quantri.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import anuong.cantho.backend.dto.quantri.AdminRequestDTO;
import anuong.cantho.backend.dto.quantri.AdminResponseDTO;
import anuong.cantho.backend.repository.quantri.AdminRepository;
import anuong.cantho.backend.service.quantri.AdminService;
import anuong.cantho.backend.entity.quantri.Admin;

@Service
public class AdminServiceImpl implements AdminService {

  private final AdminRepository adminRepository;
  private final PasswordEncoder passwordEncoder;

  public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
    this.adminRepository = adminRepository;
    this.passwordEncoder = passwordEncoder;
  }

  private AdminResponseDTO mapToDTO(Admin admin) {
    AdminResponseDTO dto = new AdminResponseDTO();
    dto.setId(admin.getId());
    dto.setTenDangNhap(admin.getTenDangNhap());
    dto.setHoTen(admin.getHoTen());
    dto.setEmail(admin.getEmail());
    dto.setSoDienThoai(admin.getSoDienThoai());
    dto.setAnhDaiDien(admin.getAnhDaiDien());
    dto.setHoatDong(admin.getHoatDong());
    dto.setLanDangNhapCuoi(admin.getLanDangNhapCuoi());
    dto.setNgayTao(admin.getNgayTao());
    dto.setNgayCapNhat(admin.getNgayCapNhat());
    return dto;
  }

  @Override
  public List<AdminResponseDTO> findAll() {
    return adminRepository.findAll().stream().map(this::mapToDTO).toList();
  }

  @Override
  public AdminResponseDTO findById(Long id) {
    Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
    return mapToDTO(admin);
  }

  @Override
  public AdminResponseDTO create(AdminRequestDTO dto) {
    Admin admin = new Admin();
    admin.setTenDangNhap(dto.getTenDangNhap());
    admin.setMatKhau(passwordEncoder.encode(dto.getMatKhau()));
    admin.setHoTen(dto.getHoTen());
    admin.setEmail(dto.getEmail());
    admin.setSoDienThoai(dto.getSoDienThoai());
    admin.setAnhDaiDien(dto.getAnhDaiDien());
    admin.setHoatDong(true);
    admin.setNgayTao(LocalDateTime.now());
    admin.setNgayCapNhat((LocalDateTime.now()));

    return mapToDTO(adminRepository.save(admin));
  }

  @Override
  public AdminResponseDTO update(Long id, AdminRequestDTO dto) {
    Admin admin = adminRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Admin not found"));
    admin.setHoTen(dto.getHoTen());
    admin.setEmail(dto.getEmail());
    admin.setSoDienThoai(dto.getSoDienThoai());
    admin.setAnhDaiDien(dto.getAnhDaiDien());
    admin.setHoatDong(dto.getHoatDong());
    admin.setNgayCapNhat(LocalDateTime.now());

    return mapToDTO(adminRepository.save(admin));
  }

  @Override
  public void delete(Long id) {
    adminRepository.deleteById(id);
  }

  @Override
  public AdminResponseDTO login(String tenDangNhap, String matKhau) {

    Admin admin = adminRepository.findByTenDangNhap(tenDangNhap)
        .orElseThrow(() -> new RuntimeException("Sai tài khoản"));

    if (!passwordEncoder.matches(matKhau, admin.getMatKhau())) {
      throw new RuntimeException("Sai mật khẩu");
    }

    admin.setLanDangNhapCuoi(LocalDateTime.now());

    adminRepository.save(admin);

    return mapToDTO(admin);
  }
}
