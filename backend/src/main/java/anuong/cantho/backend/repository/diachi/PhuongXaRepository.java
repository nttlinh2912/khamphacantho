package anuong.cantho.backend.repository.diachi;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.diachi.PhuongXa;

@Repository
public interface PhuongXaRepository extends JpaRepository<PhuongXa, Long> {

  // Kiểm tra mã phường/xã đã tồn tại, không phân biệt chữ hoa/chữ thường
  boolean existsByMaPhuongXaIgnoreCase(String maPhuongXa);

  // Kiểm tra mã trùng với bản ghi khác khi cập nhật
  boolean existsByMaPhuongXaIgnoreCaseAndIdNot(
      String maPhuongXa,
      Long id);

  // Kiểm tra đường dẫn SEO đã tồn tại
  boolean existsByDuongDanSeoIgnoreCase(String duongDanSeo);

  // Kiểm tra đường dẫn SEO trùng với bản ghi khác khi cập nhật
  boolean existsByDuongDanSeoIgnoreCaseAndIdNot(
      String duongDanSeo,
      Long id);

  // Lấy danh sách phường/xã theo tỉnh/thành
  List<PhuongXa> findByTinhThanh_IdOrderByTenPhuongXaAsc(Long tinhThanhId);

  // Lấy danh sách phường/xã đang hoạt động theo tỉnh/thành
  List<PhuongXa> findByTinhThanh_IdAndHoatDongTrueOrderByTenPhuongXaAsc(
      Long tinhThanhId);

  // Lấy một phường/xã theo ID, kèm thông tin tỉnh/thành
  @Query("""
          SELECT px
          FROM PhuongXa px
          JOIN FETCH px.tinhThanh
          WHERE px.id = :id
      """)
  Optional<PhuongXa> findByIdWithTinhThanh(@Param("id") Long id);

  // Danh sách có tìm kiếm, lọc tỉnh/thành, lọc trạng thái và phân trang
  @Query(value = """
          SELECT px
          FROM PhuongXa px
          JOIN FETCH px.tinhThanh
          WHERE
              (:tinhThanhId IS NULL OR px.tinhThanh.id = :tinhThanhId)
              AND (:hoatDong IS NULL OR px.hoatDong = :hoatDong)
              AND (
                  :keyword IS NULL
                  OR LOWER(px.tenPhuongXa) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(px.maPhuongXa) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(px.duongDanSeo) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(px.tinhThanh.tenTinhThanh) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
      """, countQuery = """
          SELECT COUNT(px)
          FROM PhuongXa px
          WHERE
              (:tinhThanhId IS NULL OR px.tinhThanh.id = :tinhThanhId)
              AND (:hoatDong IS NULL OR px.hoatDong = :hoatDong)
              AND (
                  :keyword IS NULL
                  OR LOWER(px.tenPhuongXa) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(px.maPhuongXa) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(px.duongDanSeo) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(px.tinhThanh.tenTinhThanh) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
      """)
  Page<PhuongXa> search(
      @Param("keyword") String keyword,
      @Param("tinhThanhId") Long tinhThanhId,
      @Param("hoatDong") Boolean hoatDong,
      Pageable pageable);
}
