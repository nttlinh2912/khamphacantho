package anuong.cantho.backend.entity.diachi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "phuong_xa",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_phuong_xa_ma",
            columnNames = "ma_phuong_xa"
        ),
        @UniqueConstraint(
            name = "uk_phuong_xa_slug",
            columnNames = "duong_dan_seo"
        )
    }
)
@NoArgsConstructor
@Getter
@Setter
public class PhuongXa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ten_phuong_xa", nullable = false, length = 255)
  private String tenPhuongXa;

  @Column(name = "ma_phuong_xa", nullable = false, length = 50)
  private String maPhuongXa;

  @Column(name = "duong_dan_seo", length = 255)
  private String duongDanSeo;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "tinh_thanh_id", nullable = false)
  private TinhThanh tinhThanh;

  @Column(name = "hoat_dong", nullable = false)
  private Boolean hoatDong = true;
}
