package anuong.cantho.backend.dto.baiviet.baiviet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaiVietRequestDTO {

  @NotNull (message = "Vui lòng chọn danh mục bài viết")
  private Long danhMucBaiVietId;

  @NotBlank(message = "Tiêu đề bài viết không được để trống")
  @Size(max = 500)
  private String tieuDe;

  @NotBlank(message = "Đường dẫn SEO không được để trống")
  @Size(max = 255)
  private String duongDanSeo;

  @Size(max = 1000)
  private String tomTat;

  private String noiDung;

  @Size(max = 2048)
  private String hinhDaiDien;

  @Size(max = 500)
  private String hinhDaiDienAlt;

  @Size(max = 255)
  private String tuKhoaChinh;

  @Size(max = 255)
  private String tieuDeSeo;

  @Size(max = 320)
  private String moTaSeo;

  @Size(max = 1000)
  private String tuKhoaSeo;

  private Boolean hoatDong;

  private Boolean noiBat;
}
