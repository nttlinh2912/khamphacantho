package anuong.cantho.backend.dto.baiviet.baiviet.seo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhanTichSeoResponseDTO {

  private int tongDiem;

  private int tongTieuChi;

  private int tieuChiDat;

  private int tieuChiChuaDat;

  private int soTuNoiDung;

  private double matDoTuKhoa;

  private List<KetQuaTieuChiSeoDTO> ketQua;

}
