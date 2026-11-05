package anuong.cantho.backend.service.quanan;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import anuong.cantho.backend.dto.quanan.danhmuc.CapNhatGiaTriDanhMucDTO;
import anuong.cantho.backend.dto.quanan.danhmuc.DanhMucRequestDTO;
import anuong.cantho.backend.dto.quanan.danhmuc.DanhMucResponseDTO;

public interface DanhMucService {

   // Danh sách có phân trang
    Page<DanhMucResponseDTO> getAll(Pageable pageable);

    // Danh sách đầy đủ
    List<DanhMucResponseDTO> getAll();

    // Chi tiết theo ID
    DanhMucResponseDTO getById(Long id);

    // Chi tiết theo slug
    DanhMucResponseDTO getBySlug(String slug);

    // Danh mục đang hoạt động
    List<DanhMucResponseDTO> getDanhMucHoatDong();

    // Danh mục nổi bật
    List<DanhMucResponseDTO> getDanhMucNoiBat();

    // Tìm kiếm có phân trang
    Page<DanhMucResponseDTO> timKiem(
        String keyword,
        Pageable pageable
    );

    // Thêm mới
    DanhMucResponseDTO create(DanhMucRequestDTO request);

    // Cập nhật
    DanhMucResponseDTO update(Long id, DanhMucRequestDTO request);

    // Cập nhật trạng thái hoạt động
    DanhMucResponseDTO capNhatTrangThai(
        Long id,
        CapNhatGiaTriDanhMucDTO request
    );

    // Cập nhật trạng thái nổi bật
    DanhMucResponseDTO capNhatNoiBat(
        Long id,
        CapNhatGiaTriDanhMucDTO request
    );

    // Xóa danh mục
    void delete(Long id);
}
