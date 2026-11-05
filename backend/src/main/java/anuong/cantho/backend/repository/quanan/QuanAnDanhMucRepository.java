package anuong.cantho.backend.repository.quanan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.quanan.QuanAnDanhMuc;

@Repository
public interface QuanAnDanhMucRepository extends JpaRepository<QuanAnDanhMuc, Long> {

  // Kiểm tra danh mục có đang được liên kết với quán ăn hay không
  boolean existsByDanhMuc_Id(Long danhMucId);

  // Đếm số liên kết để hiển thị thông tin nếu cần
  long countByDanhMuc_Id(Long danhMucId);

  long countByQuanAn_Id(Long quanAnId);

  boolean existsByQuanAn_Id(Long quanAnId);

  // Lấy tất cả danh mục được gán cho một quán
  List<QuanAnDanhMuc> findByQuanAn_Id(Long quanAnId);

  // Lấy tất cả quán thuộc một danh mục
  List<QuanAnDanhMuc> findByDanhMuc_Id(Long danhMucId);

  // Kiểm tra một quán đã có danh mục này chưa
  boolean existsByQuanAn_IdAndDanhMuc_Id(
      Long quanAnId,
      Long danhMucId);

  // Kiểm tra trùng khi cập nhật, loại trừ bản ghi đang sửa
  boolean existsByQuanAn_IdAndDanhMuc_IdAndIdNot(
      Long quanAnId,
      Long danhMucId,
      Long id);
}
