package anuong.cantho.backend.dto.baiviet.baiviet.seo;

import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhanTichSeoRequestDTO {

  private String tieuDe;

  private String duongDanSeo;

  private String tomTat;

  private String noiDung;

  private String tieuDeSeo;

  private String moTaSeo;

  private String tuKhoaChinh;

  private String hinhDaiDien;

  @Size(max = 500)
  private String hinhDaiDienAlt;

  private List<HinhAnhSeoDTO> danhSachHinhAnh;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class HinhAnhSeoDTO {

    private String urlAnh;

    private String altText;
  }

}
