package anuong.cantho.backend.repository.baiviet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.baiviet.HinhAnhBaiViet;

@Repository 
public interface HinhAnhBaiVietRepository extends JpaRepository<HinhAnhBaiViet,Long>{

  // Lấy toàn bộ ảnh của một bài viết theo thứ tự
    List<HinhAnhBaiViet>
    findByBaiViet_IdOrderByThuTuAscIdAsc(Long baiVietId);

    // Lấy ảnh thuộc bài viết cụ thể
    List<HinhAnhBaiViet> findByBaiViet_Id(Long baiVietId);

    // Đếm số ảnh của bài viết
    long countByBaiViet_Id(Long baiVietId);

}
