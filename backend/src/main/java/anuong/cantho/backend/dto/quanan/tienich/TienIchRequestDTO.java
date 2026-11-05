package anuong.cantho.backend.dto.quanan.tienich;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TienIchRequestDTO {

  @NotBlank(message = "Tên tiện ích không được để trống")
  @Size(max = 255, message = "Tên tiện ích không được vượt quá 255 ký tự")
  private String tenTienIch;

  @Size(max = 100, message = "Icon không được vượt quá 100 ký tự")
  private String icon;

  private Boolean hoatDong = true;
}
