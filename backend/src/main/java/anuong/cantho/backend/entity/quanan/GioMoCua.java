package anuong.cantho.backend.entity.quanan;

import java.time.LocalTime;

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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gio_mo_cua", uniqueConstraints = {
    @UniqueConstraint(name = "uk_gio_mo_cua_quan_thu", columnNames = { "quan_an_id", "thu" })
}, indexes = {
    @Index(name = "idx_gio_mo_cua_quan", columnList = "quan_an_id")
})
@NoArgsConstructor
@Getter
@Setter
public class GioMoCua {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Quán ăn không được để trống")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "quan_an_id", nullable = false)
  private QuanAn quanAn;

  @NotNull(message = "Vui lòng chọn thứ trong tuần")
  @Min(value = 1, message = "Thứ phải nằm trong khoảng 1 đến 7")
  @Max(value = 7, message = "Thứ phải nằm trong khoảng 1 đến 7")
  @Column(name = "thu", nullable = false)
  private Integer thu;

  @Column(name = "gio_mo")
  private LocalTime gioMo;

  @Column(name = "gio_dong")
  private LocalTime gioDong;

  @NotNull
  @Column(name = "nghi", nullable = false)
  private Boolean nghi = false;

  @Size(max = 500)
  @Column(name = "ghi_chu", length = 500)
  private String ghiChu;

  @jakarta.persistence.PrePersist
  protected void onCreate() {
    if (nghi == null) {
      nghi = false;
    }
  }

}
