package anuong.cantho.backend.dto.diachi;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhuongXaResponseDTO {
  private Long id;

  private String tenPhuongXa;

  private String maPhuongXa;

  private String duongDanSeo;

  private Long tinhThanhId;

  private String tenTinhThanh;

  private Boolean hoatDong;

}
