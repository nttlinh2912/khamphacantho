package anuong.cantho.backend.entity.baiviet;

import java.time.LocalDateTime;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bai_viet", uniqueConstraints = {
    @UniqueConstraint(name = "uk_bai_viet_slug", columnNames = "duong_dan_seo")
}, indexes = {
    @Index(name = "idx_bai_viet_danh_muc", columnList = "danh_muc_bai_viet_id"),
    @Index(name = "idx_bai_viet_trang_thai", columnList = "hoat_dong"),
    @Index(name = "idx_bai_viet_ngay_dang", columnList = "ngay_dang")
})
@Getter
@Setter
@NoArgsConstructor
public class BaiViet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Vui lòng chọn danh mục bài viết")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "danh_muc_bai_viet_id", nullable = false)
  private DanhMucBaiViet danhMucBaiViet;

  @NotBlank(message = "Tiêu đề bài viết không được để trống")
  @Size(max = 500, message = "Tiêu đề không được vượt quá 500 ký tự")
  @Column(name = "tieu_de", nullable = false, length = 500)
  private String tieuDe;

  @NotBlank(message = "Đường dẫn SEO không được để trống")
  @Size(max = 255, message = "Đường dẫn SEO không được vượt quá 255 ký tự")
  @Column(name = "duong_dan_seo", nullable = false, length = 255)
  private String duongDanSeo;

  @Size(max = 1000, message = "Tóm tắt không được vượt quá 1000 ký tự")
  @Column(name = "tom_tat", length = 1000)
  private String tomTat;

  @Lob
  @Column(name = "noi_dung", columnDefinition = "MEDIUMTEXT")
  private String noiDung;

  @Size(max = 2048)
  @Column(name = "hinh_dai_dien", length = 2048)
  private String hinhDaiDien;

  @PositiveOrZero(message = "Lượt xem không được âm")
  @Column(name = "luot_xem", nullable = false)
  private Long luotXem = 0L;

  @NotNull
  @Column(name = "hoat_dong", nullable = false)
  private Boolean hoatDong = false;

  @NotNull
  @Column(name = "noi_bat", nullable = false)
  private Boolean noiBat = false;

  @Size(max = 255)
  @Column(name = "tieu_de_seo", length = 255)
  private String tieuDeSeo;

  @Size(max = 320)
  @Column(name = "mo_ta_seo", length = 320)
  private String moTaSeo;

  @Size(max = 1000)
  @Column(name = "tu_khoa_seo", length = 1000)
  private String tuKhoaSeo;

  @Column(name = "ngay_dang")
  private LocalDateTime ngayDang;

  @Column(name = "ngay_tao", nullable = false, updatable = false)
  private LocalDateTime ngayTao;

  @Column(name = "ngay_cap_nhat", nullable = false)
  private LocalDateTime ngayCapNhat;

  @Size(max = 500)
  @Column(name = "hinh_dai_dien_alt", length = 500)
  private String hinhDaiDienAlt;

  @Size(max = 255, message = "Từ khóa chính không được vượt quá 255 ký tự")
  @Column(name = "tu_khoa_chinh", length = 255)
  private String tuKhoaChinh;

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

    if (hoatDong == null) {
      hoatDong = false;
    }

    if (noiBat == null) {
      noiBat = false;
    }
  }

  @PreUpdate
  protected void onUpdate() {
    ngayCapNhat = LocalDateTime.now();
  }
}
