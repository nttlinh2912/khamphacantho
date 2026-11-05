package anuong.cantho.backend.repository.baiviet;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.baiviet.DanhMucBaiViet;

@Repository
public interface DanhMucBaiVietRepository extends JpaRepository<DanhMucBaiViet, Long> {
  // Lấy tất cả danh mục, sắp xếp theo thứ tự và ID
  List<DanhMucBaiViet> findAllByOrderByThuTuAscIdAsc();

  // Lấy danh mục đang hoạt động
  List<DanhMucBaiViet> findByHoatDongTrueOrderByThuTuAscIdAsc();

  // Tìm danh mục theo slug
  Optional<DanhMucBaiViet> findByDuongDanSeo(String duongDanSeo);

  // Kiểm tra slug đã tồn tại
  boolean existsByDuongDanSeo(String duongDanSeo);

  // Kiểm tra slug đã tồn tại ở danh mục khác
  boolean existsByDuongDanSeoAndIdNot(
      String duongDanSeo,
      Long id);
}
