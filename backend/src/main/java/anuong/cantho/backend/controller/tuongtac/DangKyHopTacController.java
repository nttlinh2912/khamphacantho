package anuong.cantho.backend.controller.tuongtac;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.tuongtac.dangkyhoptac.CapNhatGhiChuHopTacDTO;
import anuong.cantho.backend.dto.tuongtac.dangkyhoptac.CapNhatTrangThaiHopTacDTO;
import anuong.cantho.backend.dto.tuongtac.dangkyhoptac.DangKyHopTacRequestDTO;
import anuong.cantho.backend.dto.tuongtac.dangkyhoptac.DangKyHopTacResponseDTO;
import anuong.cantho.backend.dto.tuongtac.dangkyhoptac.ThongBaoDangKyHopTacDTO;
import anuong.cantho.backend.service.tuongtac.DangKyHopTacService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
// @CrossOrigin (origins = "http://localhost:5173")
@CrossOrigin(origins = "https://didaucantho.netlify.app")
@RequiredArgsConstructor 
public class DangKyHopTacController {

  private final DangKyHopTacService service;

    // ==========================================
    // 1. KHÁCH HÀNG GỬI ĐĂNG KÝ
    // POST /api/dang-ky-hop-tac
    // ==========================================

    @PostMapping("/api/dang-ky-hop-tac")
    public ResponseEntity<DangKyHopTacResponseDTO> taoDangKy(
            @Valid @RequestBody DangKyHopTacRequestDTO dto) {

        DangKyHopTacResponseDTO response = service.taoDangKy(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ==========================================
    // 2. DANH SÁCH TẤT CẢ ĐĂNG KÝ
    // GET /api/admin/dang-ky-hop-tac
    // ==========================================

    @GetMapping("/api/admin/dang-ky-hop-tac")
    public ResponseEntity<Page<DangKyHopTacResponseDTO>> layTatCa(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) String trangThai,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "ngayTao"
                )
        );

        return ResponseEntity.ok(
                service.layTatCa(keyword, trangThai, pageable)
        );
    }

    // ==========================================
    // 3. DANH SÁCH ĐANG CHỜ XỬ LÝ
    // GET /api/admin/dang-ky-hop-tac/cho-xu-ly
    // ==========================================

    @GetMapping("/api/admin/dang-ky-hop-tac/cho-xu-ly")
    public ResponseEntity<Page<DangKyHopTacResponseDTO>> layChoXuLy(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "ngayTao"
                )
        );

        return ResponseEntity.ok(
                service.layChoXuLy(pageable)
        );
    }

    // ==========================================
    // 4. DANH SÁCH ĐÃ DUYỆT
    // GET /api/admin/dang-ky-hop-tac/da-duyet
    // ==========================================

    @GetMapping("/api/admin/dang-ky-hop-tac/da-duyet")
    public ResponseEntity<Page<DangKyHopTacResponseDTO>> layDaDuyet(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "ngayTao"
                )
        );

        return ResponseEntity.ok(
                service.layDaDuyet(pageable)
        );
    }

    // ==========================================
    // 5. XEM CHI TIẾT ĐĂNG KÝ
    // GET /api/admin/dang-ky-hop-tac/{id}
    // ==========================================

    @GetMapping("/api/admin/dang-ky-hop-tac/{id}")
    public ResponseEntity<DangKyHopTacResponseDTO> layChiTiet(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.layChiTiet(id)
        );
    }

    // ==========================================
    // 6. CẬP NHẬT TRẠNG THÁI
    // PATCH /api/admin/dang-ky-hop-tac/{id}/trang-thai
    // ==========================================

    @PatchMapping("/api/admin/dang-ky-hop-tac/{id}/trang-thai")
    public ResponseEntity<DangKyHopTacResponseDTO> capNhatTrangThai(

            @PathVariable Long id,

            @Valid @RequestBody CapNhatTrangThaiHopTacDTO dto) {

        return ResponseEntity.ok(
                service.capNhatTrangThai(id, dto)
        );
    }

    // ==========================================
    // 7. CẬP NHẬT GHI CHÚ QUẢN TRỊ
    // PATCH /api/admin/dang-ky-hop-tac/{id}/ghi-chu
    // ==========================================

    @PatchMapping("/api/admin/dang-ky-hop-tac/{id}/ghi-chu")
    public ResponseEntity<DangKyHopTacResponseDTO> capNhatGhiChu(

            @PathVariable Long id,

            @Valid @RequestBody CapNhatGhiChuHopTacDTO dto) {

        return ResponseEntity.ok(
                service.capNhatGhiChu(id, dto)
        );
    }

    // ==========================================
    // 8. ĐẾM ĐĂNG KÝ CHỜ XỬ LÝ
    // GET /api/admin/dang-ky-hop-tac/thong-bao
    // ==========================================

    @GetMapping("/api/admin/dang-ky-hop-tac/thong-bao")
    public ResponseEntity<Long> demThongBao() {

        return ResponseEntity.ok(
                service.demChoXuLy()
        );
    }

    // ==========================================
    // 9. LẤY DANH SÁCH THÔNG BÁO MỚI
    // GET /api/admin/dang-ky-hop-tac/thong-bao/moi
    // ==========================================

    @GetMapping("/api/admin/dang-ky-hop-tac/thong-bao/moi")
    public ResponseEntity<List<ThongBaoDangKyHopTacDTO>> layThongBaoMoi(

            @RequestParam(required = false) Long tuId) {

        return ResponseEntity.ok(
                service.layThongBaoMoi(tuId)
        );
    }

}
