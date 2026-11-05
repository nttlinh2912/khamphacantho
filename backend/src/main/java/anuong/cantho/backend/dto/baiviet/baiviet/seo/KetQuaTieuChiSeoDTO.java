package anuong.cantho.backend.dto.baiviet.baiviet.seo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KetQuaTieuChiSeoDTO {

  private String maTieuChi;
  private String tenTieuChi;

  private boolean dat;

  private String thongBao;

}
