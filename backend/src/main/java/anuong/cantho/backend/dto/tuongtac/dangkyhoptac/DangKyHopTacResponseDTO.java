package anuong.cantho.backend.dto.tuongtac.dangkyhoptac;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DangKyHopTacResponseDTO {

  private Long id;

  private String hoTen;

  private String soDienThoai;

  private String email;

  private String tenQuan;

  private String diaChiQuan;

  private String noiDung;

  private String trangThai;

  private String ghiChuQuanTri;

  private LocalDateTime ngayTao;

}
