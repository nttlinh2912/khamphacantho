package anuong.cantho.backend.dto.quanan.quanandanhmuc;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuanAnDanhMucRequestDTO {

  @NotNull(message = "Quán ăn không được để trống")
  private Long quanAnId;

  @NotNull(message = "Danh mục không được để trống")
  private Long danhMucId;
}
