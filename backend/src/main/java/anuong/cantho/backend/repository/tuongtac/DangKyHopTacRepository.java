package anuong.cantho.backend.repository.tuongtac;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.tuongtac.DangKyHopTac;

@Repository
public interface DangKyHopTacRepository extends JpaRepository<DangKyHopTac, Long> {

  // ds theo trang thai
  Page<DangKyHopTac> findByTrangThai(
      String trangThai,
      Pageable pageable);

  // Tìm kiếm theo nhiều trường
  @Query("""
      SELECT d FROM DangKyHopTac d
      WHERE
          (:keyword IS NULL OR :keyword = ''
          OR LOWER(d.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%'))
          OR LOWER(d.soDienThoai) LIKE LOWER(CONCAT('%', :keyword, '%'))
          OR LOWER(d.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
          OR LOWER(d.tenQuan) LIKE LOWER(CONCAT('%', :keyword, '%'))
          OR LOWER(d.diaChiQuan) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND
          (:trangThai IS NULL OR :trangThai = ''
          OR d.trangThai = :trangThai)
      """)
  Page<DangKyHopTac> timKiemVaLoc(
      @Param("keyword") String keyword,
      @Param("trangThai") String trangThai,
      Pageable pageable);

  // Đếm số đăng ký đang chờ xử lý
  long countByTrangThai(String trangThai);

  // Lấy các đăng ký mới hơn một ID
  List<DangKyHopTac> findByIdGreaterThanOrderByIdDesc(
      Long id);

  // Lấy đăng ký mới nhất
  List<DangKyHopTac> findTop5ByOrderByNgayTaoDescIdDesc();

}
