package anuong.cantho.backend.dto.diachi;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data 
public class TinhThanhRequestDTO {

  @NotBlank (message = "Tên tỉnh thành không được để trống")
  private String tenTinhThanh;

  @NotBlank (message = "Mã tỉnh thành không được để trống")
  private String maTinhThanh;

  private String duongDanSeo;

  private Boolean hoatDong;

}
