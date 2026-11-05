package anuong.cantho.backend.dto.baiviet.baiviet;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaiVietResponseDTO {

  private Long id;

  private Long danhMucBaiVietId;
  private String tenDanhMuc;

  private String tieuDe;
  private String duongDanSeo;
  private String tomTat;
  private String noiDung;

  private String hinhDaiDien;
  private String hinhDaiDienAlt;

  private String tuKhoaChinh;
  private String tieuDeSeo;
  private String moTaSeo;
  private String tuKhoaSeo;

  private Long luotXem;

  private Boolean hoatDong;
  private Boolean noiBat;

  private LocalDateTime ngayDang;
  private LocalDateTime ngayTao;
  private LocalDateTime ngayCapNhat;

}
