package anuong.cantho.backend.controller.quantri;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.quantri.AdminRequestDTO;
import anuong.cantho.backend.dto.quantri.AdminResponseDTO;
import anuong.cantho.backend.dto.quantri.LoginRequestDTO;
import anuong.cantho.backend.service.quantri.AdminService;

@RestController 
@RequestMapping ("/api/admin") 
@CrossOrigin (origins = "http://localhost:5173")
public class AdminController {

  private final AdminService adminService;

  public AdminController(AdminService adminService){
    this.adminService = adminService;
  }

  @GetMapping
    public List<AdminResponseDTO> findAll() {
        return adminService.findAll();
    }

    @GetMapping("/{id}")
    public AdminResponseDTO findById(
            @PathVariable Long id) {

        return adminService.findById(id);
    }

    @PostMapping
    public AdminResponseDTO create(
            @RequestBody AdminRequestDTO dto) {

        return adminService.create(dto);
    }

    @PutMapping("/{id}")
    public AdminResponseDTO update(
            @PathVariable Long id,
            @RequestBody AdminRequestDTO dto) {

        return adminService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        adminService.delete(id);
    }

    @PostMapping("/login")
    public AdminResponseDTO login(
            @RequestBody LoginRequestDTO dto) {

        return adminService.login(
                dto.getTenDangNhap(),
                dto.getMatKhau()
        );
    }
}
