package anuong.cantho.backend.dto.quanan.quan;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuanAnRequestDTO {

  @NotBlank(message = "Tên quán không được để trống")
  @Size(max = 255, message = "Tên quán tối đa 255 ký tự")
  private String tenQuan;

  @Size(max = 255, message = "Đường dẫn SEO tối đa 255 ký tự")
  private String duongDanSeo;

  @Size(max = 500, message = "Địa chỉ tối đa 500 ký tự")
  private String diaChi;

  @NotNull(message = "Vui lòng chọn tỉnh/thành")
  private Long tinhThanhId;

  @NotNull(message = "Vui lòng chọn phường/xã")
  private Long phuongXaId;

  @Size(max = 20)
  private String soDienThoai;

  @Email(message = "Email không hợp lệ")
  @Size(max = 254)
  private String email;

  @Size(max = 2048)
  private String website;

  @Size(max = 2048)
  private String urlFacebook;

  @Size(max = 2048)
  private String urlTiktok;

  @Size(max = 2048)
  private String urlGoogleMap;

  @DecimalMin(value = "-90.0", message = "Vĩ độ phải từ -90 đến 90")
  @DecimalMax(value = "90.0", message = "Vĩ độ phải từ -90 đến 90")
  private BigDecimal viDo;

  @DecimalMin(value = "-180.0", message = "Kinh độ phải từ -180 đến 180")
  @DecimalMax(value = "180.0", message = "Kinh độ phải từ -180 đến 180")
  private BigDecimal kinhDo;

  @Size(max = 500)
  private String moTaNgan;

  private String moTaChiTiet;

  @Digits(integer = 15, fraction = 2)
  @DecimalMin(value = "0.0", message = "Giá từ không được âm")
  private BigDecimal giaTu;

  @Digits(integer = 15, fraction = 2)
  @DecimalMin(value = "0.0", message = "Giá đến không được âm")
  private BigDecimal giaDen;

  @DecimalMin(value = "0.0")
  @DecimalMax(value = "5.0")
  private Double diemDanhGiaGoogle;

  @PositiveOrZero
  private Integer tongDanhGiaGoogle;

  @NotNull(message = "Vui lòng chọn trạng thái nổi bật")
  private Boolean noiBat = false;

  @NotNull(message = "Vui lòng chọn trạng thái hoạt động")
  private Boolean hoatDong = false;

  @Size(max = 255)
  private String tieuDeSeo;

  @Size(max = 320)
  private String moTaSeo;

  @Size(max = 1000)
  private String tuKhoaSeo;
}
