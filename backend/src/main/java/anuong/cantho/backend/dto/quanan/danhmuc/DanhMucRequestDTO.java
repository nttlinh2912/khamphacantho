package anuong.cantho.backend.dto.quanan.danhmuc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DanhMucRequestDTO {

  @NotBlank(message = "Tên danh mục không được để trống")
  @Size(max = 255, message = "Tên danh mục tối đa 255 ký tự")
  private String tenDanhMuc;

  @Size(max = 255, message = "Đường dẫn SEO tối đa 255 ký tự")
  private String duongDanSeo;

  @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
  private String moTa;

  @Size(max = 100, message = "Icon tối đa 100 ký tự")
  private String icon;

  @NotNull(message = "Thứ tự hiển thị không được để trống")
  @PositiveOrZero(message = "Thứ tự hiển thị không được âm")
  private Integer thuTuHienThi = 0;

  @NotNull(message = "Trạng thái nổi bật không được để trống")
  private Boolean noiBat = false;

  @NotNull(message = "Trạng thái hoạt động không được để trống")
  private Boolean hoatDong = true;
}
