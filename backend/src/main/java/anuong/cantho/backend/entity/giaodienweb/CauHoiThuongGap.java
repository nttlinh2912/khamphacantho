package anuong.cantho.backend.entity.giaodienweb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
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
@Table(name = "cau_hoi_thuong_gap", indexes = {
    @Index(name = "idx_faq_hoat_dong_thu_tu", columnList = "hoat_dong, thu_tu")
})
@Getter
@Setter
@NoArgsConstructor
public class CauHoiThuongGap {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Câu hỏi không được để trống")
  @Size(max = 500, message = "Câu hỏi không được vượt quá 500 ký tự")
  @Column(name = "cau_hoi", length = 500, nullable = false)
  private String cauHoi;

  @NotBlank(message = "Câu trả lời không được để trống")
  @Lob
  @Column(name = "cau_tra_loi", nullable = false)
  private String cauTraLoi;

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
