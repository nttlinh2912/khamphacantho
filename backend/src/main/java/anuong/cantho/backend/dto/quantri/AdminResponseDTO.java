package anuong.cantho.backend.dto.quantri;

import java.time.LocalDateTime;

import lombok.Data;

@Data 
public class AdminResponseDTO {
  private Long id;
  private String tenDangNhap;
  private String hoTen;
  private String email;
  private String soDienThoai;
  private String anhDaiDien;
  private Boolean hoatDong;
  private LocalDateTime lanDangNhapCuoi;
  private LocalDateTime ngayTao;
  private LocalDateTime ngayCapNhat;
}
