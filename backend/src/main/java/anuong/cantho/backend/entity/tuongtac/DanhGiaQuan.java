package anuong.cantho.backend.entity.tuongtac;

import java.time.LocalDateTime;

import anuong.cantho.backend.entity.quanan.QuanAn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "danh_gia_quan", indexes = {
    @Index(name = "idx_danh_gia_quan_an", columnList = "quan_an_id"),
    @Index(name = "idx_danh_gia_duyet_hien_thi", columnList = "da_duyet, hien_thi"),
    @Index(name = "idx_danh_gia_ngay_tao", columnList = "ngay_tao")
})
@Getter
@Setter
@NoArgsConstructor
public class DanhGiaQuan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Quán ăn không được để trống")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "quan_an_id", nullable = false, foreignKey = @ForeignKey(name = "fk_danh_gia_quan_an"))
  private QuanAn quanAn;

  @NotBlank(message = "Tên người đánh giá không được để trống")
  @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
  @Column(name = "ten_nguoi_danh_gia", length = 100, nullable = false)
  private String tenNguoiDanhGia;

  @Pattern(regexp = "^(0[35789])[0-9]{8}$", message = "Số điện thoại không đúng định dạng")
  @Size(max = 10, message = "Số điện thoại không được vượt quá 10 ký tự")
  @Column(name = "so_dien_thoai", length = 10)
  private String soDienThoai;

  @NotNull(message = "Vui lòng chọn số sao")
  @Min(value = 1, message = "Số sao tối thiểu là 1")
  @Max(value = 5, message = "Số sao tối đa là 5")
  @Column(name = "so_sao", nullable = false)
  private Integer soSao;

  @Size(max = 3000, message = "Nội dung không được vượt quá 3000 ký tự")
  @Column(name = "noi_dung", length = 3000)
  private String noiDung;

  @NotNull
  @Column(name = "da_duyet", nullable = false)
  private Boolean daDuyet = false;

  @NotNull
  @Column(name = "hien_thi", nullable = false)
  private Boolean hienThi = false;

  @Column(name = "ngay_tao", nullable = false, updatable = false)
  private LocalDateTime ngayTao;

  @PrePersist
  protected void onCreate() {
    if (ngayTao == null) {
      ngayTao = LocalDateTime.now();
    }

    if (daDuyet == null) {
      daDuyet = false;
    }

    if (hienThi == null) {
      hienThi = false;
    }
  }
}
