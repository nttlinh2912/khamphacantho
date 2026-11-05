package anuong.cantho.backend.entity.quanan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hinh_anh_quan", indexes = {
    @Index(name = "idx_hinh_anh_quan_thu_tu", columnList = "quan_an_id, thu_tu")
})
@NoArgsConstructor
@Getter
@Setter
public class HinhAnhQuan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Quán ăn không được để trống")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "quan_an_id", nullable = false)
  private QuanAn quanAn;

  @NotBlank(message = "URL ảnh không được để trống")
  @Size(max = 2048)
  @Column(name = "url_anh", nullable = false, length = 2048)
  private String urlAnh;

  @Size(max = 50)
  @Column(name = "loai_anh", length = 50)
  private String loaiAnh;

  @Size(max = 500)
  @Column(name = "chu_thich", length = 500)
  private String chuThich;

  @NotNull
  @Column(name = "anh_dai_dien", nullable = false)
  private Boolean anhDaiDien = false;

  @PositiveOrZero(message = "Thứ tự ảnh không được âm")
  @Column(name = "thu_tu", nullable = false)
  private Integer thuTu = 0;

  @jakarta.persistence.PrePersist
  protected void onCreate() {
    if (anhDaiDien == null) {
      anhDaiDien = false;
    }

    if (thuTu == null) {
      thuTu = 0;
    }
  }
}
