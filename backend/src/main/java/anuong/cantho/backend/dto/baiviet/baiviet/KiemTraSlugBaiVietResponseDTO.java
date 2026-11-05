package anuong.cantho.backend.dto.baiviet.baiviet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KiemTraSlugBaiVietResponseDTO {

  private String slug;
  private Boolean daTonTai;
  private Boolean hopLe;

}
