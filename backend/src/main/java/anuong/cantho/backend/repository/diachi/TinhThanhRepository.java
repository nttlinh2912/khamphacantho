package anuong.cantho.backend.repository.diachi;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.diachi.TinhThanh;

@Repository 
public interface TinhThanhRepository extends JpaRepository<TinhThanh, Long>{

  Optional<TinhThanh> findByMaTinhThanh(String maTinhThanh);
  boolean existsByMaTinhThanh (String maTinhThanh);

}
