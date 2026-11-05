package anuong.cantho.backend.repository.quanan;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.quanan.QuanAn;

@Repository
public interface QuanAnRepository extends JpaRepository<QuanAn, Long> {

  boolean existsByDuongDanSeo(String duongDanSeo);

  boolean existsByDuongDanSeoAndIdNot(
      String duongDanSeo,
      Long id);

  Optional<QuanAn> findByDuongDanSeo(String duongDanSeo);

  // Danh sách quán đang hoạt động dành cho khách truy cập
  Page<QuanAn> findByHoatDongTrue(Pageable pageable);

  // Tìm kiếm và lọc quán
  @Query("""
      SELECT q
      FROM QuanAn q
      WHERE
          (
              :keyword IS NULL
              OR LOWER(q.tenQuan)
                  LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(COALESCE(q.diaChi, ''))
                  LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(COALESCE(q.moTaNgan, ''))
                  LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
          AND (:tinhThanhId IS NULL OR q.tinhThanh.id = :tinhThanhId)
          AND (:phuongXaId IS NULL OR q.phuongXa.id = :phuongXaId)
          AND (:hoatDong IS NULL OR q.hoatDong = :hoatDong)
          AND (:noiBat IS NULL OR q.noiBat = :noiBat)
      """)
  Page<QuanAn> timKiemVaLoc(
      @Param("keyword") String keyword,
      @Param("tinhThanhId") Long tinhThanhId,
      @Param("phuongXaId") Long phuongXaId,
      @Param("hoatDong") Boolean hoatDong,
      @Param("noiBat") Boolean noiBat,
      Pageable pageable);
}
