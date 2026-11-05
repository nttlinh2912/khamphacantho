package anuong.cantho.backend.dto.baiviet.hinhanhbaiviet;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CapNhatThuTuHinhAnhDTO {

  @NotNull(message = "ID hình ảnh không được để trống")
  private Long id;

  @NotNull(message = "Thứ tự không được để trống")
  @PositiveOrZero(message = "Thứ tự không được âm")
  private Integer thuTu;

}
