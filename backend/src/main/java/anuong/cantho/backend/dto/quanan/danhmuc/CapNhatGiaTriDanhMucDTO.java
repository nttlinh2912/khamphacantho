package anuong.cantho.backend.dto.quanan.danhmuc;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CapNhatGiaTriDanhMucDTO {

  @NotNull (message = "Giá trị trạng thái không được để trống")
  private Boolean giaTri;

}
