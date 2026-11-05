package anuong.cantho.backend.dto.quanan.tienich;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuanAnTienIchRequestDTO {
  @NotNull(message = "ID quán ăn không được để trống")
  @Positive(message = "ID quán ăn phải lớn hơn 0")
  private Long quanAnId;

  @NotNull(message = "ID tiện ích không được để trống")
  @Positive(message = "ID tiện ích phải lớn hơn 0")
  private Long tienIchId;

}
