package anuong.cantho.backend.dto.quanan.giomocua;

import java.time.LocalTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GioMoCuaRequestDTO {
  @NotNull(message = "Vui lòng chọn quán ăn")
  private Long quanAnId;

  @NotNull(message = "Vui lòng chọn thứ trong tuần")
  @Min(value = 1, message = "Thứ phải nằm trong khoảng 1 đến 7")
  @Max(value = 7, message = "Thứ phải nằm trong khoảng 1 đến 7")
  private Integer thu;

  private LocalTime gioMo;

  private LocalTime gioDong;

  @NotNull(message = "Vui lòng xác định quán có nghỉ hay không")
  private Boolean nghi;

  @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
  private String ghiChu;
}
