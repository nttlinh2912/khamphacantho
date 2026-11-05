package anuong.cantho.backend.dto.baiviet.baiviet;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CapNhatTrangThaiBaiVietDTO {

  @NotNull(message = "Trạng thái không được để trống")
  private Boolean hoatDong;

}
