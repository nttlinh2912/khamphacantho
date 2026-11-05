package anuong.cantho.backend.service.quanan;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.quanan.tienich.TienIchRequestDTO;
import anuong.cantho.backend.dto.quanan.tienich.TienIchResponseDTO;
import anuong.cantho.backend.entity.quanan.TienIch;
import anuong.cantho.backend.repository.quanan.QuanAnTienIchRepository;
import anuong.cantho.backend.repository.quanan.TienIchRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TienIchService {

  private final TienIchRepository tienIchRepository;

  private final QuanAnTienIchRepository quanAnTienIchRepository;

  // Lấy tất cả tiện ích
  @Transactional(readOnly = true)
  public List<TienIchResponseDTO> layTatCa() {
    return tienIchRepository.findAllByOrderByTenTienIchAsc()
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  // Lấy tiện ích đang hoạt động
  @Transactional(readOnly = true)
  public List<TienIchResponseDTO> layDangHoatDong() {
    return tienIchRepository.findByHoatDongTrueOrderByTenTienIchAsc()
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  // Lấy tiện ích theo ID
  @Transactional(readOnly = true)
  public TienIchResponseDTO layTheoId(Long id) {
    TienIch tienIch = timTheoId(id);
    return toResponseDTO(tienIch);
  }

  // Thêm tiện ích
  public TienIchResponseDTO them(TienIchRequestDTO dto) {
    String ten = chuanHoaTen(dto.getTenTienIch());

    if (tienIchRepository.existsByTenTienIchIgnoreCase(ten)) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Tên tiện ích đã tồn tại");
    }

    TienIch tienIch = new TienIch();
    tienIch.setTenTienIch(ten);
    tienIch.setIcon(chuanHoaIcon(dto.getIcon()));

    // Mặc định hoạt động nếu không truyền giá trị
    tienIch.setHoatDong(
        dto.getHoatDong() == null ? true : dto.getHoatDong());

    return toResponseDTO(tienIchRepository.save(tienIch));
  }

  // Cập nhật tiện ích
  public TienIchResponseDTO capNhat(
      Long id,
      TienIchRequestDTO dto) {
    TienIch tienIch = timTheoId(id);
    String ten = chuanHoaTen(dto.getTenTienIch());

    if (tienIchRepository.existsByTenTienIchIgnoreCaseAndIdNot(
        ten, id)) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Tên tiện ích đã được sử dụng");
    }

    tienIch.setTenTienIch(ten);
    tienIch.setIcon(chuanHoaIcon(dto.getIcon()));

    if (dto.getHoatDong() != null) {
      tienIch.setHoatDong(dto.getHoatDong());
    }

    return toResponseDTO(tienIchRepository.save(tienIch));
  }

  // Bật hoặc tắt tiện ích
  public TienIchResponseDTO doiTrangThai(Long id) {
    TienIch tienIch = timTheoId(id);

    tienIch.setHoatDong(!Boolean.TRUE.equals(tienIch.getHoatDong()));

    return toResponseDTO(tienIchRepository.save(tienIch));
  }

  // Xóa tiện ích
  public void xoa(Long id) {
    TienIch tienIch = timTheoId(id);

    if (quanAnTienIchRepository.existsByTienIch_Id(id)) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Không thể xóa tiện ích đã được gán cho quán. "
              + "Hãy tắt trạng thái hoạt động nếu không muốn sử dụng nữa.");
    }

    tienIchRepository.delete(tienIch);
  }

  // Tìm tiện ích theo ID
  private TienIch timTheoId(Long id) {
    return tienIchRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Không tìm thấy tiện ích có ID: " + id));
  }

  // Chuẩn hóa tên
  private String chuanHoaTen(String ten) {
    if (ten == null || ten.trim().isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Tên tiện ích không được để trống");
    }

    return ten.trim();
  }

  // Chuẩn hóa icon
  private String chuanHoaIcon(String icon) {
    if (icon == null || icon.trim().isEmpty()) {
      return null;
    }

    return icon.trim();
  }

  // Chuyển entity sang DTO
  private TienIchResponseDTO toResponseDTO(TienIch tienIch) {
    TienIchResponseDTO dto = new TienIchResponseDTO();

    dto.setId(tienIch.getId());
    dto.setTenTienIch(tienIch.getTenTienIch());
    dto.setIcon(tienIch.getIcon());
    dto.setHoatDong(tienIch.getHoatDong());

    return dto;
  }
}
