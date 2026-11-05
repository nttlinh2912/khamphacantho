package anuong.cantho.backend.repository.quanan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.quanan.TienIch;

@Repository 
public interface TienIchRepository extends JpaRepository<TienIch, Long>{

  // Kiểm tra tên tiện ích đã tồn tại
    boolean existsByTenTienIchIgnoreCase(String tenTienIch);

    // Kiểm tra tên tiện ích đã tồn tại ở bản ghi khác
    boolean existsByTenTienIchIgnoreCaseAndIdNot(
            String tenTienIch,
            Long id
    );

    // Lấy danh sách tiện ích đang hoạt động
    List<TienIch> findByHoatDongTrueOrderByTenTienIchAsc();

    // Lấy toàn bộ tiện ích theo tên
    List<TienIch> findAllByOrderByTenTienIchAsc();
}
