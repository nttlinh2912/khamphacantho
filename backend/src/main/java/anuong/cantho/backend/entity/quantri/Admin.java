package anuong.cantho.backend.entity.quantri;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor 
@Getter
@Setter
public class Admin {
  @Id 
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;

  private String tenDangNhap;

  private String matKhau;

  private String hoTen;

  private String email;

  private String soDienThoai;

  private String anhDaiDien;

  private Boolean hoatDong;

  private LocalDateTime lanDangNhapCuoi;

  private LocalDateTime ngayTao;

  private LocalDateTime ngayCapNhat;
}
