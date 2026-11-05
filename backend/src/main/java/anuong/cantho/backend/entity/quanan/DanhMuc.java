package anuong.cantho.backend.entity.quanan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "danh_muc", uniqueConstraints = {
    @UniqueConstraint(name = "uk_danh_muc_slug", columnNames = "duong_dan_seo")
}, indexes = {
    @Index(name = "idx_danh_muc_hoat_dong", columnList = "hoat_dong")
})
@NoArgsConstructor
@Getter
@Setter
public class DanhMuc {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Tên danh mục không được để trống")
  @Size(max = 255)
  @Column(name = "ten_danh_muc", nullable = false, length = 255)
  private String tenDanhMuc;

  @Size(max = 255)
  @Column(name = "duong_dan_seo", length = 255)
  private String duongDanSeo;

  @Size(max = 1000)
  @Column(name = "mo_ta", length = 1000)
  private String moTa;

  @Size(max = 100)
  @Column(name = "icon", length = 100)
  private String icon;

  @PositiveOrZero(message = "Thứ tự hiển thị không được âm")
  @Column(name = "thu_tu_hien_thi", nullable = false)
  private Integer thuTuHienThi = 0;

  @NotNull
  @Column(name = "noi_bat", nullable = false)
  private Boolean noiBat = false;

  @NotNull
  @Column(name = "hoat_dong", nullable = false)
  private Boolean hoatDong = true;

  @jakarta.persistence.PrePersist
  protected void onCreate() {
    if (thuTuHienThi == null) {
      thuTuHienThi = 0;
    }

    if (noiBat == null) {
      noiBat = false;
    }

    if (hoatDong == null) {
      hoatDong = true;
    }
  }
}
