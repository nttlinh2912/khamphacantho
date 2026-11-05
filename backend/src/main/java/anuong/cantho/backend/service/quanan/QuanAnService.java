package anuong.cantho.backend.service.quanan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import anuong.cantho.backend.dto.quanan.quan.CapNhatTrangThaiQuanAnDTO;
import anuong.cantho.backend.dto.quanan.quan.QuanAnRequestDTO;
import anuong.cantho.backend.dto.quanan.quan.QuanAnResponseDTO;

public interface QuanAnService {

  Page<QuanAnResponseDTO> getAll(
      String keyword,
      Long tinhThanhId,
      Long phuongXaId,
      Boolean hoatDong,
      Boolean noiBat,
      Pageable pageable);

  Page<QuanAnResponseDTO> getQuanAnHoatDong(
      Pageable pageable);

  QuanAnResponseDTO getById(Long id);

  QuanAnResponseDTO getBySlug(String slug);

  QuanAnResponseDTO create(QuanAnRequestDTO request);

  QuanAnResponseDTO update(
      Long id,
      QuanAnRequestDTO request);

  QuanAnResponseDTO capNhatTrangThai(
      Long id,
      CapNhatTrangThaiQuanAnDTO request);

  QuanAnResponseDTO capNhatNoiBat(
      Long id,
      CapNhatTrangThaiQuanAnDTO request);

  void xoaMem(Long id);

  QuanAnResponseDTO khoiPhuc(Long id);
}
