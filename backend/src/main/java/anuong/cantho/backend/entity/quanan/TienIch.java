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
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tien_ich", uniqueConstraints = {
    @UniqueConstraint(name = "uk_tien_ich_ten", columnNames = "ten_tien_ich")
}, indexes = {
    @Index(name = "idx_tien_ich_hoat_dong", columnList = "hoat_dong")
})
@Getter
@Setter
@NoArgsConstructor
public class TienIch {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Tên tiện ích không được để trống")
  @Size(max = 255)
  @Column(name = "ten_tien_ich", nullable = false, length = 255)
  private String tenTienIch;

  @Size(max = 100)
  @Column(name = "icon", length = 100)
  private String icon;

  @NotNull
  @Column(name = "hoat_dong", nullable = false)
  private Boolean hoatDong = true;

  @jakarta.persistence.PrePersist
  protected void onCreate() {
    if (hoatDong == null) {
      hoatDong = true;
    }
  }
}
