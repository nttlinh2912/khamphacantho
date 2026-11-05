package anuong.cantho.backend.dto.quanan.quan;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CapNhatTrangThaiQuanAnDTO {

  @NotNull(message = "Giá trị không được để trống")
  private Boolean giaTri;

}
