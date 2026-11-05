package anuong.cantho.backend.service.diachi;

import java.util.List;

import org.springframework.data.domain.Page;

import anuong.cantho.backend.dto.diachi.PhuongXaRequestDTO;
import anuong.cantho.backend.dto.diachi.PhuongXaResponseDTO;

public interface PhuongXaService {

  // Lấy toàn bộ phường/xã
  List<PhuongXaResponseDTO> getAll();

  // Lấy danh sách có tìm kiếm, lọc và phân trang
  Page<PhuongXaResponseDTO> search(
      String keyword,
      Long tinhThanhId,
      Boolean hoatDong,
      int page,
      int size,
      String sortBy,
      String sortDir);

  // Lấy chi tiết một phường/xã
  PhuongXaResponseDTO getById(Long id);

  // Lấy danh sách phường/xã thuộc tỉnh/thành
  List<PhuongXaResponseDTO> getByTinhThanhId(Long tinhThanhId);

  // Thêm phường/xã
  PhuongXaResponseDTO create(PhuongXaRequestDTO request);

  // Cập nhật phường/xã
  PhuongXaResponseDTO update(Long id, PhuongXaRequestDTO request);

  // Xóa phường/xã
  void delete(Long id);
}
