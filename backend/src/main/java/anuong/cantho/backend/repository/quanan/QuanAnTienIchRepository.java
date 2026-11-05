package anuong.cantho.backend.repository.quanan;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.quanan.QuanAnTienIch;

@Repository
public interface QuanAnTienIchRepository extends JpaRepository<QuanAnTienIch, Long> {

  long countByQuanAn_Id(Long quanAnId);

  boolean existsByQuanAn_Id(Long quanAnId);

  // Lấy danh sách tiện ích đã gán cho một quán
    List<QuanAnTienIch> findByQuanAn_IdOrderByTienIch_TenTienIchAsc(
            Long quanAnId
    );

    // Kiểm tra tiện ích đã được gán cho quán chưa
    boolean existsByQuanAn_IdAndTienIch_Id(
            Long quanAnId,
            Long tienIchId
    );

    // Tìm quan hệ giữa một quán và một tiện ích
    Optional<QuanAnTienIch> findByQuanAn_IdAndTienIch_Id(
            Long quanAnId,
            Long tienIchId
    );

    // Kiểm tra tiện ích có đang được quán nào sử dụng không
    boolean existsByTienIch_Id(Long tienIchId);

    // Lấy tất cả quán đang sử dụng tiện ích
    List<QuanAnTienIch> findByTienIch_Id(Long tienIchId);
}
