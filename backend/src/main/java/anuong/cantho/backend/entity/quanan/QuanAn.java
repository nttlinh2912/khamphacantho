package anuong.cantho.backend.entity.quanan;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import anuong.cantho.backend.entity.diachi.PhuongXa;
import anuong.cantho.backend.entity.diachi.TinhThanh;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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

@Entity
@Table(name = "quan_an", uniqueConstraints = {
    @UniqueConstraint(name = "uk_quan_an_slug", columnNames = "duong_dan_seo")
}, indexes = {
    @Index(name = "idx_quan_an_tinh_phuong", columnList = "tinh_thanh_id, phuong_xa_id"),
    @Index(name = "idx_quan_an_trang_thai", columnList = "hoat_dong"),
    @Index(name = "idx_quan_an_noi_bat", columnList = "noi_bat")
})
@NoArgsConstructor
@Getter
@Setter
public class QuanAn {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Tên quán không được để trống")
  @Size(max = 255, message = "Tên quán tối đa 255 ký tự")
  @Column(name = "ten_quan", nullable = false, length = 255)
  private String tenQuan;

  @Size(max = 255, message = "Slug tối đa 255 ký tự")
  @Column(name = "duong_dan_seo", length = 255)
  private String duongDanSeo;

  @Size(max = 500, message = "Địa chỉ tối đa 500 ký tự")
  @Column(name = "dia_chi", length = 500)
  private String diaChi;

  @NotNull(message = "Vui lòng chọn tỉnh/thành")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tinh_thanh_id", nullable = false)
  private TinhThanh tinhThanh;

  @NotNull(message = "Vui lòng chọn phường/xã")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "phuong_xa_id", nullable = false)
  private PhuongXa phuongXa;

  @Size(max = 20, message = "Số điện thoại tối đa 20 ký tự")
  @Column(name = "so_dien_thoai", length = 20)
  private String soDienThoai;

  @Email(message = "Email không hợp lệ")
  @Size(max = 254, message = "Email tối đa 254 ký tự")
  @Column(name = "email", length = 254)
  private String email;

  @Size(max = 2048)
  @Column(name = "website", length = 2048)
  private String website;

  @Size(max = 2048)
  @Column(name = "url_facebook", length = 2048)
  private String urlFacebook;

  @Size(max = 2048)
  @Column(name = "url_tiktok", length = 2048)
  private String urlTiktok;

  @Size(max = 2048)
  @Column(name = "url_google_map", length = 2048)
  private String urlGoogleMap;

  @DecimalMin(value = "-90.0")
  @DecimalMax(value = "90.0")
  @Column(name = "vi_do", precision = 10, scale = 7)
  private BigDecimal viDo;

  @DecimalMin(value = "-180.0")
  @DecimalMax(value = "180.0")
  @Column(name = "kinh_do", precision = 10, scale = 7)
  private BigDecimal kinhDo;

  @Size(max = 500)
  @Column(name = "mo_ta_ngan", length = 500)
  private String moTaNgan;

  @Lob
  @Column(name = "mo_ta_chi_tiet")
  private String moTaChiTiet;

  @Digits(integer = 15, fraction = 2)
  @DecimalMin(value = "0.0")
  @Column(name = "gia_tu", precision = 17, scale = 2)
  private BigDecimal giaTu;

  @Digits(integer = 15, fraction = 2)
  @DecimalMin(value = "0.0")
  @Column(name = "gia_den", precision = 17, scale = 2)
  private BigDecimal giaDen;

  @DecimalMin(value = "0.0")
  @DecimalMax(value = "5.0")
  @Column(name = "diem_danh_gia_google")
  private Double diemDanhGiaGoogle;

  @PositiveOrZero
  @Column(name = "tong_danh_gia_google")
  private Integer tongDanhGiaGoogle;

  @PositiveOrZero
  @Column(name = "luot_xem", nullable = false)
  private Long luotXem = 0L;

  @NotNull
  @Column(name = "noi_bat", nullable = false)
  private Boolean noiBat = false;

  @NotNull
  @Column(name = "hoat_dong", nullable = false)
  private Boolean hoatDong = false;

  @Size(max = 255)
  @Column(name = "tieu_de_seo", length = 255)
  private String tieuDeSeo;

  @Size(max = 320)
  @Column(name = "mo_ta_seo", length = 320)
  private String moTaSeo;

  @Size(max = 1000)
  @Column(name = "tu_khoa_seo", length = 1000)
  private String tuKhoaSeo;

  @Column(name = "ngay_tao", nullable = false, updatable = false)
  private LocalDateTime ngayTao;

  @Column(name = "ngay_cap_nhat", nullable = false)
  private LocalDateTime ngayCapNhat;

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();

    if (ngayTao == null) {
      ngayTao = now;
    }

    ngayCapNhat = now;

    if (luotXem == null) {
      luotXem = 0L;
    }

    if (noiBat == null) {
      noiBat = false;
    }

    if (hoatDong == null) {
      hoatDong = false;
    }
  }

  @PreUpdate
  protected void onUpdate() {
    ngayCapNhat = LocalDateTime.now();
  }

}
