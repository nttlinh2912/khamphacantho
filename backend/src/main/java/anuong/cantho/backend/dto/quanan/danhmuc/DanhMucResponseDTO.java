package anuong.cantho.backend.dto.quanan.danhmuc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DanhMucResponseDTO {

  private Long id;

  private String tenDanhMuc;

  private String duongDanSeo;

  private String moTa;

  private String icon;

  private Integer thuTuHienThi;

  private Boolean noiBat;

  private Boolean hoatDong;

  // Constructor có 8 tham số
  public DanhMucResponseDTO(
      Long id,
      String tenDanhMuc,
      String duongDanSeo,
      String moTa,
      String icon,
      Integer thuTuHienThi,
      Boolean noiBat,
      Boolean hoatDong) {
    this.id = id;
    this.tenDanhMuc = tenDanhMuc;
    this.duongDanSeo = duongDanSeo;
    this.moTa = moTa;
    this.icon = icon;
    this.thuTuHienThi = thuTuHienThi;
    this.noiBat = noiBat;
    this.hoatDong = hoatDong;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTenDanhMuc() {
    return tenDanhMuc;
  }

  public void setTenDanhMuc(String tenDanhMuc) {
    this.tenDanhMuc = tenDanhMuc;
  }

  public String getDuongDanSeo() {
    return duongDanSeo;
  }

  public void setDuongDanSeo(String duongDanSeo) {
    this.duongDanSeo = duongDanSeo;
  }

  public String getMoTa() {
    return moTa;
  }

  public void setMoTa(String moTa) {
    this.moTa = moTa;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public Integer getThuTuHienThi() {
    return thuTuHienThi;
  }

  public void setThuTuHienThi(Integer thuTuHienThi) {
    this.thuTuHienThi = thuTuHienThi;
  }

  public Boolean getNoiBat() {
    return noiBat;
  }

  public void setNoiBat(Boolean noiBat) {
    this.noiBat = noiBat;
  }

  public Boolean getHoatDong() {
    return hoatDong;
  }

  public void setHoatDong(Boolean hoatDong) {
    this.hoatDong = hoatDong;
  }
}
