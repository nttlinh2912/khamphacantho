package anuong.cantho.backend.entity.diachi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter 
@Setter 
public class TinhThanh {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String tenTinhThanh;

  @Column(unique = true, nullable = false)
  private String maTinhThanh;

  @Column(unique = true)
  private String duongDanSeo;

  private Boolean hoatDong;
}
