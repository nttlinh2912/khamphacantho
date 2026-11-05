package anuong.cantho.backend.entity.baiviet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
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
@Table(name = "danh_muc_bai_viet", uniqueConstraints = {
    @UniqueConstraint(name = "uk_danh_muc_bai_viet_slug", columnNames = "duong_dan_seo")
}, indexes = {
    @Index(name = "idx_danh_muc_bai_viet_hoat_dong", columnList = "hoat_dong")
})
@Getter
@Setter
@NoArgsConstructor
public class DanhMucBaiViet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Tên danh mục không được để trống")
  @Size(max = 255)
  @Column(name = "ten_danh_muc", nullable = false, length = 255)
  private String tenDanhMuc;

  @NotBlank(message = "Đường dẫn SEO không được để trống")
  @Size(max = 255)
  @Column(name = "duong_dan_seo", nullable = false, length = 255)
  private String duongDanSeo;

  @Size(max = 1000)
  @Column(name = "mo_ta", length = 1000)
  private String moTa;

  @PositiveOrZero(message = "Thứ tự không được âm")
  @Column(name = "thu_tu", nullable = false)
  private Integer thuTu = 0;

  @NotNull 
  @Column(name = "hoat_dong", nullable = false)
  private Boolean hoatDong = true;

  @PrePersist
  protected void onCreate() {
    if (thuTu == null) {
      thuTu = 0;
    }

    if (hoatDong == null) {
      hoatDong = true;
    }
  }
}
