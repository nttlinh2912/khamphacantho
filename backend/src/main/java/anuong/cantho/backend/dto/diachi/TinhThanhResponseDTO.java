package anuong.cantho.backend.dto.diachi;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class TinhThanhResponseDTO {

  private Long id;

  private String tenTinhThanh;
  private String maTinhThanh;
  private String duongDanSeo;
  private Boolean hoatDong;

  public TinhThanhResponseDTO(Long id, String tenTinhThanh, String maTinhThanh, String duongDanSeo, Boolean hoatDong) {
    this.id = id;
    this.tenTinhThanh = tenTinhThanh;
    this.maTinhThanh = maTinhThanh;
    this.duongDanSeo = duongDanSeo;
    this.hoatDong = hoatDong;
  }
}
