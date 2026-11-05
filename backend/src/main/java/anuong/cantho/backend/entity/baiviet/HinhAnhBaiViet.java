package anuong.cantho.backend.entity.baiviet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "hinh_anh_bai_viet", indexes = {
    @Index(name = "idx_hinh_anh_bai_viet_thu_tu", columnList = "bai_viet_id, thu_tu")
})
@Getter
@Setter
@NoArgsConstructor
public class HinhAnhBaiViet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Bài viết không được để trống")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "bai_viet_id", nullable = false)
  private BaiViet baiViet;

  @NotBlank(message = "URL ảnh không được để trống")
  @Size(max = 2048, message = "URL ảnh không được vượt quá 2048 ký tự")
  @Column(name = "url_anh", nullable = false, length = 2048)
  private String urlAnh;

  @Size(max = 500, message = "Chú thích không được vượt quá 500 ký tự")
  @Column(name = "chu_thich", length = 500)
  private String chuThich;

  @PositiveOrZero(message = "Thứ tự ảnh không được âm")
  @Column(name = "thu_tu", nullable = false)
  private Integer thuTu = 0;

  @Size(max = 500)
  @Column(name = "alt_text", length = 500)
  private String altText;

  @PrePersist
  protected void onCreate() {
    if (thuTu == null) {
      thuTu = 0;
    }
  }
}
