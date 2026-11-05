package anuong.cantho.backend.entity.cauhinh;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cau_hinh_website")
@Getter
@Setter
@NoArgsConstructor
public class CauHinhWebsite {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Tên website không được để trống")
  @Size(max = 200, message = "Tên website không được vượt quá 200 ký tự")
  @Column(name = "ten_website", length = 200, nullable = false)
  private String tenWebsite;

  @Size(max = 255, message = "Slogan không được vượt quá 255 ký tự")
  @Column(name = "slogan", length = 255)
  private String slogan;

  @Size(max = 2048, message = "Đường dẫn logo quá dài")
  @Column(name = "logo", columnDefinition = "TEXT")
  private String logo;

  @Size(max = 2048, message = "Đường dẫn favicon quá dài")
  @Column(name = "favicon", columnDefinition = "TEXT")
  private String favicon;

  @Email(message = "Email không đúng định dạng")
  @Size(max = 254, message = "Email không được vượt quá 254 ký tự")
  @Column(name = "email", length = 254)
  private String email;

  @Pattern(regexp = "^$|^(0[35789])[0-9]{8}$", message = "Số điện thoại không đúng định dạng")
  @Column(name = "so_dien_thoai", length = 10)
  private String soDienThoai;

  @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
  @Column(name = "dia_chi", length = 500)
  private String diaChi;

  @Size(max = 2048)
  @Column(name = "facebook", columnDefinition = "TEXT")
  private String facebook;

  @Size(max = 2048)
  @Column(name = "zalo", columnDefinition = "TEXT")
  private String zalo;

  @Size(max = 2048)
  @Column(name = "youtube", columnDefinition = "TEXT")
  private String youtube;

  @Size(max = 2048)
  @Column(name = "instagram", columnDefinition = "TEXT")
  private String instagram;

  @Size(max = 2048)
  @Column(name = "tiktok", columnDefinition = "TEXT")
  private String tiktok;

  @Size(max = 100)
  @Column(name = "ma_google_analytics", length = 100)
  private String maGoogleAnalytics;

  @Size(max = 500)
  @Column(name = "ma_google_search_console", length = 500)
  private String maGoogleSearchConsole;

  @Size(max = 100)
  @Column(name = "ma_facebook_pixel", length = 100)
  private String maFacebookPixel;

  @Size(max = 255)
  @Column(name = "tieu_de_seo_trang_chu", length = 255)
  private String tieuDeSeoTrangChu;

  @Size(max = 320)
  @Column(name = "mo_ta_seo_trang_chu", length = 320)
  private String moTaSeoTrangChu;

  @Size(max = 1000)
  @Column(name = "tu_khoa_seo_trang_chu", length = 1000)
  private String tuKhoaSeoTrangChu;

  @Lob
  @Column(name = "chan_trang_website", columnDefinition = "TEXT")
  private String chanTrangWebsite;

  // Banner trang chủ
  @Size(max = 2048, message = "Đường dẫn banner quá dài")
  @Column(name = "banner_trang_chu", columnDefinition = "TEXT")
  private String bannerTrangChu;

  @Size(max = 500, message = "Mô tả ảnh không được vượt quá 500 ký tự")
  @Column(name = "banner_alt", length = 500)
  private String bannerAlt;

  @Size(max = 255, message = "Tiêu đề banner không được vượt quá 255 ký tự")
  @Column(name = "banner_tieu_de", length = 255)
  private String bannerTieuDe;

  @Size(max = 1000, message = "Mô tả banner không được vượt quá 1000 ký tự")
  @Column(name = "banner_mo_ta", length = 1000)
  private String bannerMoTa;

  @Size(max = 2048, message = "Đường dẫn liên kết banner quá dài")
  @Column(name = "banner_link", columnDefinition = "TEXT")
  private String bannerLink;

  @Column(name = "hien_thi_banner", nullable = false)
  private Boolean hienThiBanner = true;

  @PrePersist
  protected void onCreate() {
    chuanHoaDuLieu();
  }

  @PreUpdate
  protected void onUpdate() {
    chuanHoaDuLieu();
  }

  private void chuanHoaDuLieu() {
    if (tenWebsite != null) {
      tenWebsite = tenWebsite.trim();
    }

    if (slogan != null) {
      slogan = slogan.trim();
    }

    if (email != null) {
      email = email.trim().toLowerCase();
    }

    if (soDienThoai != null) {
      soDienThoai = soDienThoai.trim();
    }
  }
}
