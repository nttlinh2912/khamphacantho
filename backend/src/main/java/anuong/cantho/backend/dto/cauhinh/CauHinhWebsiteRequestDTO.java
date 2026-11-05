package anuong.cantho.backend.dto.cauhinh;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CauHinhWebsiteRequestDTO {

  // ==============================
  // THÔNG TIN THƯƠNG HIỆU
  // ==============================

  @NotBlank(message = "Tên website không được để trống")
  @Size(max = 200, message = "Tên website không được vượt quá 200 ký tự")
  private String tenWebsite;

  @Size(max = 255, message = "Slogan không được vượt quá 255 ký tự")
  private String slogan;

  // ==============================
  // BANNER TRANG CHỦ
  // ==============================

  @Size(max = 2048, message = "Đường dẫn banner quá dài")
  private String bannerTrangChu;

  @Size(max = 500, message = "Mô tả ảnh không được vượt quá 500 ký tự")
  private String bannerAlt;

  @Size(max = 255, message = "Tiêu đề banner không được vượt quá 255 ký tự")
  private String bannerTieuDe;

  @Size(max = 1000, message = "Mô tả banner không được vượt quá 1000 ký tự")
  private String bannerMoTa;

  @Size(max = 2048, message = "Đường dẫn liên kết banner quá dài")
  private String bannerLink;

  private Boolean hienThiBanner = true;

  // ==============================
  // THÔNG TIN LIÊN HỆ
  // ==============================

  @Email(message = "Email không đúng định dạng")
  @Size(max = 254, message = "Email không được vượt quá 254 ký tự")
  private String email;

  @Pattern(regexp = "^$|^(0[35789])[0-9]{8}$", message = "Số điện thoại không đúng định dạng")
  private String soDienThoai;

  @Size(max = 500)
  private String diaChi;

  // ==============================
  // MẠNG XÃ HỘI
  // ==============================

  @Size(max = 2048)
  private String facebook;

  @Size(max = 2048)
  private String zalo;

  @Size(max = 2048)
  private String youtube;

  @Size(max = 2048)
  private String instagram;

  @Size(max = 2048)
  private String tiktok;

  // ==============================
  // GOOGLE ANALYTICS
  // ==============================

  @Size(max = 100)
  private String maGoogleAnalytics;

  @Size(max = 500)
  private String maGoogleSearchConsole;

  @Size(max = 100)
  private String maFacebookPixel;

  // ==============================
  // SEO TRANG CHỦ
  // ==============================

  @Size(max = 255)
  private String tieuDeSeoTrangChu;

  @Size(max = 320)
  private String moTaSeoTrangChu;

  @Size(max = 1000)
  private String tuKhoaSeoTrangChu;

  // ==============================
  // CHÂN TRANG WEBSITE
  // ==============================

  private String chanTrangWebsite;

}
