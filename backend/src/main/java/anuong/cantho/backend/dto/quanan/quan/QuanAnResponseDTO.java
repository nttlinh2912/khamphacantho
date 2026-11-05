package anuong.cantho.backend.dto.quanan.quan;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuanAnResponseDTO {

  private Long id;

  private String tenQuan;
  private String duongDanSeo;
  private String diaChi;

  private Long tinhThanhId;
  private String tenTinhThanh;

  private Long phuongXaId;
  private String tenPhuongXa;

  private String soDienThoai;
  private String email;

  private String website;
  private String urlFacebook;
  private String urlTiktok;
  private String urlGoogleMap;

  private BigDecimal viDo;
  private BigDecimal kinhDo;

  private String moTaNgan;
  private String moTaChiTiet;

  private BigDecimal giaTu;
  private BigDecimal giaDen;

  private Double diemDanhGiaGoogle;
  private Integer tongDanhGiaGoogle;
  private Long luotXem;

  private Boolean noiBat;
  private Boolean hoatDong;

  private String tieuDeSeo;
  private String moTaSeo;
  private String tuKhoaSeo;

  private LocalDateTime ngayTao;
  private LocalDateTime ngayCapNhat;

  private Long soGioMoCua;
  private Long soHinhAnh;
  private Long soDanhMuc;
  private Long soTienIch;
}
