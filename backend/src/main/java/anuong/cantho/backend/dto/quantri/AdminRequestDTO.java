package anuong.cantho.backend.dto.quantri;

import lombok.Data;

@Data 
public class AdminRequestDTO {

  private String tenDangNhap;
  private String matKhau;
  private String hoTen;
  private String email;
  private String soDienThoai;
  private String anhDaiDien;
  private Boolean hoatDong;

}
