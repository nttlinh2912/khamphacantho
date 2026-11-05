package anuong.cantho.backend.dto.tuongtac.dangkyhoptac;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CapNhatTrangThaiHopTacDTO {

  @NotBlank(message = "Vui lòng chọn trạng thái")
  @Pattern(regexp = "^(DANG_LIEN_HE|DA_DUYET|TU_CHOI)$", message = "Trạng thái không hợp lệ")
  private String trangThai;

}
