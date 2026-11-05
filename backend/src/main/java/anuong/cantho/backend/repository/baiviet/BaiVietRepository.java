package anuong.cantho.backend.repository.baiviet;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import anuong.cantho.backend.entity.baiviet.BaiViet;

@Repository
public interface BaiVietRepository extends JpaRepository<BaiViet, Long> {

  // Kiểm tra danh mục có bài viết hay không
  boolean existsByDanhMucBaiViet_Id(Long danhMucBaiVietId);

  // Tìm bài viết theo slug
  Optional<BaiViet> findByDuongDanSeoAndHoatDongTrue(String duongDanSeo);

  // Kiểm tra slug đã tồn tại
  boolean existsByDuongDanSeo(String duongDanSeo);

  // Kiểm tra slug đã tồn tại, loại trừ bài viết đang cập nhật
  boolean existsByDuongDanSeoAndIdNot(String duongDanSeo, Long id);

  // Danh sách bài viết theo trạng thái
  Page<BaiViet> findByHoatDong(Boolean hoatDong, Pageable pageable);

  // Danh sách bài viết nổi bật đã xuất bản
  Page<BaiViet> findByHoatDongTrueAndNoiBatTrueOrderByNgayDangDesc(
      Pageable pageable);

  // Bài viết mới nhất đã xuất bản
  Page<BaiViet> findByHoatDongTrueOrderByNgayDangDesc(
      Pageable pageable);

  // Bài viết theo danh mục
  Page<BaiViet> findByDanhMucBaiViet_IdAndHoatDongTrueOrderByNgayDangDesc(
      Long danhMucId,
      Pageable pageable);

  // Tìm bài viết theo tiêu đề
  Page<BaiViet> findByTieuDeContainingIgnoreCaseAndHoatDongTrue(
      String tieuDe,
      Pageable pageable);

  // Bài viết liên quan: cùng danh mục, loại trừ bài viết hiện tại
  @Query("""
          SELECT bv
          FROM BaiViet bv
          WHERE bv.danhMucBaiViet.id = :danhMucId
            AND bv.id <> :baiVietId
            AND bv.hoatDong = true
          ORDER BY bv.ngayDang DESC
      """)
  Page<BaiViet> findBaiVietLienQuan(
      @Param("danhMucId") Long danhMucId,
      @Param("baiVietId") Long baiVietId,
      Pageable pageable);

  // Tìm kiếm và lọc dành cho trang quản trị
  @Query("""
          SELECT bv
          FROM BaiViet bv
          WHERE (:keyword IS NULL OR :keyword = ''
                 OR LOWER(bv.tieuDe) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:danhMucId IS NULL OR bv.danhMucBaiViet.id = :danhMucId)
            AND (:hoatDong IS NULL OR bv.hoatDong = :hoatDong)
            AND (:noiBat IS NULL OR bv.noiBat = :noiBat)
          ORDER BY bv.ngayTao DESC
      """)
  Page<BaiViet> timKiemQuanTri(
      @Param("keyword") String keyword,
      @Param("danhMucId") Long danhMucId,
      @Param("hoatDong") Boolean hoatDong,
      @Param("noiBat") Boolean noiBat,
      Pageable pageable);

  // Tăng lượt xem trực tiếp trong database
  @Modifying
  @Transactional 
  @Query("""
          UPDATE BaiViet bv
          SET bv.luotXem = bv.luotXem + 1
          WHERE bv.id = :id
            AND bv.hoatDong = true
      """)
  int tangLuotXem(@Param("id") Long id);

  @Query("""
        SELECT bv
        FROM BaiViet bv
        JOIN FETCH bv.danhMucBaiViet dm
        WHERE bv.duongDanSeo = :slug
          AND bv.hoatDong = true
          AND bv.ngayDang IS NOT NULL
          AND bv.ngayDang <= :thoiDiem
    """)
    Optional<BaiViet> timBaiVietDaXuatBanTheoSlug(
            @Param("slug") String slug,
            @Param("thoiDiem") LocalDateTime thoiDiem
    );
}
