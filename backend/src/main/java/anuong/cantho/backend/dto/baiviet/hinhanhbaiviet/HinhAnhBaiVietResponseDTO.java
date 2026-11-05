package anuong.cantho.backend.dto.baiviet.hinhanhbaiviet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhBaiVietResponseDTO {

  private Long id;

  private Long baiVietId;

  private String urlAnh;

  private String altText;

  private String chuThich;

  private Integer thuTu;

}
