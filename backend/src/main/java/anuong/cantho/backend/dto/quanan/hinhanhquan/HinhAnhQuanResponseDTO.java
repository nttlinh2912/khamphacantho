package anuong.cantho.backend.dto.quanan.hinhanhquan;

import anuong.cantho.backend.entity.quanan.HinhAnhQuan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhQuanResponseDTO {
  private Long id;
  private Long quanAnId;
  private String urlAnh;
  private String loaiAnh;
  private String chuThich;
  private Boolean anhDaiDien;
  private Integer thuTu;

  public static HinhAnhQuanResponseDTO fromEntity(HinhAnhQuan entity) {
    return new HinhAnhQuanResponseDTO(
        entity.getId(),
        entity.getQuanAn().getId(),
        entity.getUrlAnh(),
        entity.getLoaiAnh(),
        entity.getChuThich(),
        entity.getAnhDaiDien(),
        entity.getThuTu());
  }
}
