package anuong.cantho.backend.repository.cauhinh;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anuong.cantho.backend.entity.cauhinh.CauHinhWebsite;

@Repository 
public interface CauHinhWebsiteRepository extends JpaRepository<CauHinhWebsite,Long>{

  // Lấy cấu hình website đầu tiên
  Optional<CauHinhWebsite> findFirstByOrderByIdAsc();

}
