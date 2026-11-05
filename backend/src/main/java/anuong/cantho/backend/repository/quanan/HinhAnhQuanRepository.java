package anuong.cantho.backend.repository.quanan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.quanan.HinhAnhQuan;

@Repository
public interface HinhAnhQuanRepository extends JpaRepository<HinhAnhQuan, Long> {

  long countByQuanAn_Id(Long quanAnId);

  boolean existsByQuanAn_Id(Long quanAnId);

  // Lấy tất cả ảnh của một quán, sắp xếp theo thứ tự tăng dần
  List<HinhAnhQuan> findByQuanAn_IdOrderByThuTuAscIdAsc(Long quanAnId);

  // Lấy tất cả ảnh, sắp xếp theo thứ tự tăng dần
  List<HinhAnhQuan> findAllByOrderByThuTuAscIdAsc();

  // Tìm các ảnh đang được đặt làm ảnh đại diện của một quán
  List<HinhAnhQuan> findByQuanAn_IdAndAnhDaiDienTrue(Long quanAnId);

  // Kiểm tra quán có ảnh đại diện hay chưa
  boolean existsByQuanAn_IdAndAnhDaiDienTrue(Long quanAnId);
}
