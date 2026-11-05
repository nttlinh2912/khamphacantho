package anuong.cantho.backend.dto.quanan.tienich;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuanAnTienIchResponseDTO {

  private Long id;

  private Long quanAnId;

  private Long tienIchId;

  private String tenTienIch;

  private String icon;

  private Boolean hoatDong;
}
