package anuong.cantho.backend.repository.quantri;

import anuong.cantho.backend.entity.quantri.Admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface AdminRepository extends JpaRepository<Admin, Long> {

  Optional<Admin> findByTenDangNhap(String tenDangNhap);
  Optional<Admin> findByEmail(String email);
  boolean existsByTenDangNhap(String tenDangNhap);
  boolean existsByEmail(String email);

}
