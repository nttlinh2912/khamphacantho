package anuong.cantho.backend.entity.quanan;

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
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quan_an_tien_ich", uniqueConstraints = {
    @UniqueConstraint(name = "uk_quan_an_tien_ich", columnNames = { "quan_an_id", "tien_ich_id" })
}, indexes = {
    @Index(name = "idx_qati_tien_ich", columnList = "tien_ich_id")
})
@NoArgsConstructor
@Getter
@Setter
public class QuanAnTienIch {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Quán ăn không được để trống")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "quan_an_id", nullable = false)
  private QuanAn quanAn;

  @NotNull(message = "Tiện ích không được để trống")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tien_ich_id", nullable = false)
  private TienIch tienIch;
}
