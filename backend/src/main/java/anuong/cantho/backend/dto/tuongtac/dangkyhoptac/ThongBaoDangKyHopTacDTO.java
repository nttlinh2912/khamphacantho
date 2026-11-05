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
public class ThongBaoDangKyHopTacDTO {

  private Long id;
  
  private String hoTen;

  private String tenQuan;

  private LocalDateTime ngayTao;

  private String trangThai;


}
