package anuong.cantho.backend.service.tuongtac;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import anuong.cantho.backend.dto.tuongtac.dangkyhoptac.CapNhatGhiChuHopTacDTO;
import anuong.cantho.backend.dto.tuongtac.dangkyhoptac.CapNhatTrangThaiHopTacDTO;
import anuong.cantho.backend.dto.tuongtac.dangkyhoptac.DangKyHopTacRequestDTO;
import anuong.cantho.backend.dto.tuongtac.dangkyhoptac.DangKyHopTacResponseDTO;
import anuong.cantho.backend.dto.tuongtac.dangkyhoptac.ThongBaoDangKyHopTacDTO;
import anuong.cantho.backend.entity.tuongtac.DangKyHopTac;
import anuong.cantho.backend.repository.tuongtac.DangKyHopTacRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class DangKyHopTacService {

  private final DangKyHopTacRepository repository;

    // ==========================================
    // 1. KHÁCH HÀNG GỬI ĐĂNG KÝ
    // ==========================================

    @Transactional 
    public DangKyHopTacResponseDTO taoDangKy(
            DangKyHopTacRequestDTO dto) {

        DangKyHopTac dangKy = new DangKyHopTac();

        dangKy.setHoTen(dto.getHoTen().trim());
        dangKy.setSoDienThoai(dto.getSoDienThoai().trim());
        dangKy.setEmail(dto.getEmail().trim());
        dangKy.setTenQuan(dto.getTenQuan().trim());
        dangKy.setDiaChiQuan(dto.getDiaChiQuan().trim());
        dangKy.setNoiDung(dto.getNoiDung());

        // Mặc định chờ xử lý
        dangKy.setTrangThai("CHO_XU_LY");

        DangKyHopTac saved = repository.save(dangKy);

        return toResponseDTO(saved);
    }

    // ==========================================
    // 2. DANH SÁCH TẤT CẢ ĐĂNG KÝ
    // ==========================================

    @Transactional(readOnly = true)
    public Page<DangKyHopTacResponseDTO> layTatCa(
            String keyword,
            String trangThai,
            Pageable pageable) {

        return repository.timKiemVaLoc(
                keyword,
                trangThai,
                pageable
        ).map(this::toResponseDTO);
    }

    // ==========================================
    // 3. DANH SÁCH CHỜ XỬ LÝ
    // ==========================================

    @Transactional(readOnly = true)
    public Page<DangKyHopTacResponseDTO> layChoXuLy(
            Pageable pageable) {

        return repository.findByTrangThai(
                "CHO_XU_LY",
                pageable
        ).map(this::toResponseDTO);
    }

    // ==========================================
    // 4. DANH SÁCH ĐÃ DUYỆT
    // ==========================================

    @Transactional(readOnly = true)
    public Page<DangKyHopTacResponseDTO> layDaDuyet(
            Pageable pageable) {

        return repository.findByTrangThai(
                "DA_DUYET",
                pageable
        ).map(this::toResponseDTO);
    }

    // ==========================================
    // 5. XEM CHI TIẾT
    // ==========================================

    @Transactional(readOnly = true)
    public DangKyHopTacResponseDTO layChiTiet(Long id) {

        DangKyHopTac dangKy = timTheoId(id);

        return toResponseDTO(dangKy);
    }

    // ==========================================
    // 6. CẬP NHẬT TRẠNG THÁI
    // ==========================================

    @Transactional
    public DangKyHopTacResponseDTO capNhatTrangThai(
            Long id,
            CapNhatTrangThaiHopTacDTO dto) {

        DangKyHopTac dangKy = timTheoId(id);

        String trangThaiMoi = dto.getTrangThai();

        dangKy.setTrangThai(trangThaiMoi);

        return toResponseDTO(repository.save(dangKy));
    }

    // ==========================================
    // 7. CẬP NHẬT GHI CHÚ QUẢN TRỊ
    // ==========================================

    @Transactional
    public DangKyHopTacResponseDTO capNhatGhiChu(
            Long id,
            CapNhatGhiChuHopTacDTO dto) {

        DangKyHopTac dangKy = timTheoId(id);

        dangKy.setGhiChuQuanTri(dto.getGhiChuQuanTri());

        return toResponseDTO(repository.save(dangKy));
    }

    // ==========================================
    // 8. ĐẾM SỐ ĐĂNG KÝ CHỜ XỬ LÝ
    // ==========================================

    @Transactional(readOnly = true)
    public long demChoXuLy() {

        return repository.countByTrangThai("CHO_XU_LY");
    }

    // ==========================================
    // 9. THÔNG BÁO ĐĂNG KÝ MỚI
    // ==========================================

    @Transactional(readOnly = true)
    public List<ThongBaoDangKyHopTacDTO> layThongBaoMoi(
            Long tuId) {

        List<DangKyHopTac> danhSach;

        if (tuId == null || tuId <= 0) {

            danhSach = repository.findTop5ByOrderByNgayTaoDescIdDesc();

        } else {

            danhSach = repository
                    .findByIdGreaterThanOrderByIdDesc(tuId);
        }

        return danhSach.stream()
                .map(d -> new ThongBaoDangKyHopTacDTO(
                        d.getId(),
                        d.getHoTen(),
                        d.getTenQuan(),
                        d.getNgayTao(),
                        d.getTrangThai()
                ))
                .toList();
    }

    // ==========================================
    // HÀM TÌM THEO ID
    // ==========================================

    private DangKyHopTac timTheoId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy đăng ký hợp tác có ID: " + id
                        )
                );
    }

    // ==========================================
    // CHUYỂN ENTITY SANG RESPONSE DTO
    // ==========================================

    private DangKyHopTacResponseDTO toResponseDTO(
            DangKyHopTac d) {

        return new DangKyHopTacResponseDTO(
                d.getId(),
                d.getHoTen(),
                d.getSoDienThoai(),
                d.getEmail(),
                d.getTenQuan(),
                d.getDiaChiQuan(),
                d.getNoiDung(),
                d.getTrangThai(),
                d.getGhiChuQuanTri(),
                d.getNgayTao()
        );
    }

}
