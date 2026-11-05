package anuong.cantho.backend.dto.baiviet.hinhanhbaiviet;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HinhAnhBaiVietRequestDTO {

  @Size(max = 500, message = "ALT ảnh không được vượt quá 500 ký tự")
  private String altText;

  @Size(max = 500, message = "Chú thích không được vượt quá 500 ký tự")
  private String chuThich;

  @PositiveOrZero(message = "Thứ tự ảnh không được âm")
  private Integer thuTu;

}
