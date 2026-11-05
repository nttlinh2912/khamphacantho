package anuong.cantho.backend.service.quanan;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import anuong.cantho.backend.dto.quanan.hinhanhquan.HinhAnhQuanResponseDTO;

public interface HinhAnhQuanService {

  // Lấy tất cả hình ảnh
  List<HinhAnhQuanResponseDTO> layTatCa();

  // Lấy hình ảnh theo quán ăn
  List<HinhAnhQuanResponseDTO> layTheoQuanAn(Long quanAnId);

  // Tải lên một hoặc nhiều hình ảnh
  List<HinhAnhQuanResponseDTO> taiLen(
      Long quanAnId,
      MultipartFile[] files,
      String chuThich);

  // Chọn ảnh đại diện
  HinhAnhQuanResponseDTO datAnhDaiDien(Long id);

  // Cập nhật thứ tự ảnh
  HinhAnhQuanResponseDTO capNhatThuTu(
      Long id,
      Integer thuTu);

  // Xóa ảnh
  void xoa(Long id);
}
