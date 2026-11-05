package anuong.cantho.backend.entity.tuongtac;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dang_ky_hop_tac")
@Getter
@Setter
@NoArgsConstructor
public class DangKyHopTac {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Họ tên không được để trống")
  @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
  @Column(name = "ho_ten", length = 100, nullable = false)
  private String hoTen;

  @NotBlank(message = "Số điện thoại không được để trống")
  @Pattern(regexp = "^(0[35789])[0-9]{8}$", message = "Số điện thoại không đúng định dạng")
  @Column(name = "so_dien_thoai", length = 10, nullable = false)
  private String soDienThoai;

  @NotBlank(message = "Email không được để trống")
  @Email(message = "Email không đúng định dạng")
  @Size(max = 254, message = "Email không được vượt quá 254 ký tự")
  @Column(name = "email", length = 254, nullable = false)
  private String email;

  @NotBlank(message = "Tên quán không được để trống")
  @Size(max = 200, message = "Tên quán không được vượt quá 200 ký tự")
  @Column(name = "ten_quan", length = 200, nullable = false)
  private String tenQuan;

  @NotBlank(message = "Địa chỉ quán không được để trống")
  @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
  @Column(name = "dia_chi_quan", length = 500, nullable = false)
  private String diaChiQuan;

  @Lob
  @Column(name = "noi_dung")
  private String noiDung;

  @NotBlank(message = "Trạng thái không được để trống")
  @Size(max = 30)
  @Column(name = "trang_thai", length = 30, nullable = false)
  private String trangThai;

  @Size(max = 2000, message = "Ghi chú không được vượt quá 2000 ký tự")
  @Column(name = "ghi_chu_quan_tri", length = 2000)
  private String ghiChuQuanTri;

  @Column(name = "ngay_tao", nullable = false, updatable = false)
  private LocalDateTime ngayTao;

  @PrePersist
  protected void onCreate() {
    if (ngayTao == null) {
      ngayTao = LocalDateTime.now();
    }

    if (trangThai == null || trangThai.isBlank()) {
      trangThai = "CHO_XU_LY";
    }
  }
}
