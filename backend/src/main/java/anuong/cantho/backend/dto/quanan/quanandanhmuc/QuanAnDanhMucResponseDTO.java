package anuong.cantho.backend.dto.quanan.quanandanhmuc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuanAnDanhMucResponseDTO {

  private Long id;
  private Long quanAnId;
  private Long danhMucId;
}
