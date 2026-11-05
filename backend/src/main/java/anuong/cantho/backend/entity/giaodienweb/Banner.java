package anuong.cantho.backend.entity.giaodienweb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "banner", indexes = {
    @Index(name = "idx_banner_hoat_dong_thu_tu", columnList = "hoat_dong, thu_tu")
})
@Getter
@Setter
@NoArgsConstructor
public class Banner {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Tiêu đề banner không được để trống")
  @Size(max = 200, message = "Tiêu đề không được vượt quá 200 ký tự")
  @Column(name = "tieu_de", length = 200, nullable = false)
  private String tieuDe;

  @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
  @Column(name = "mo_ta", length = 1000)
  private String moTa;

  @NotBlank(message = "Đường dẫn ảnh không được để trống")
  @Size(max = 2048, message = "Đường dẫn ảnh quá dài")
  @Column(name = "url_anh", length = 2048, nullable = false)
  private String urlAnh;

  @Size(max = 2048, message = "Đường dẫn không được vượt quá 2048 ký tự")
  @Column(name = "duong_dan", length = 2048)
  private String duongDan;

  @NotNull(message = "Thứ tự không được để trống")
  @PositiveOrZero(message = "Thứ tự không được là số âm")
  @Column(name = "thu_tu", nullable = false)
  private Integer thuTu = 0;

  @NotNull(message = "Trạng thái hoạt động không được để trống")
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
