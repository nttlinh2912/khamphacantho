package anuong.cantho.backend.dto.diachi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhuongXaRequestDTO {

  @NotBlank(message = "Tên phường/xã không được để trống")
  @Size(max = 255, message = "Tên phường/xã không được vượt quá 255 ký tự")
  private String tenPhuongXa;

  @NotBlank(message = "Mã phường/xã không được để trống")
  @Size(max = 50, message = "Mã phường/xã không được vượt quá 50 ký tự")
  private String maPhuongXa;

  @Size(max = 255, message = "Đường dẫn SEO không được vượt quá 255 ký tự")
  @Pattern(regexp = "^$|^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Đường dẫn SEO chỉ được chứa chữ thường, số và dấu gạch ngang")
  private String duongDanSeo;

  @NotNull (message = "Vui lòng chọn tỉnh/thành")
  private Long tinhThanhId;

  private Boolean hoatDong;
}
