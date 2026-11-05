package anuong.cantho.backend.dto.quanan.tienich;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TienIchResponseDTO {

  private Long id;

  private String tenTienIch;

  private String icon;

  private Boolean hoatDong;
}
