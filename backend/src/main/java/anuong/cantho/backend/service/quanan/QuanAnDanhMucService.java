package anuong.cantho.backend.service.quanan;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.dto.quanan.quanandanhmuc.QuanAnDanhMucRequestDTO;
import anuong.cantho.backend.dto.quanan.quanandanhmuc.QuanAnDanhMucResponseDTO;
import anuong.cantho.backend.entity.quanan.DanhMuc;
import anuong.cantho.backend.entity.quanan.QuanAn;
import anuong.cantho.backend.entity.quanan.QuanAnDanhMuc;
import anuong.cantho.backend.repository.quanan.DanhMucRepository;
import anuong.cantho.backend.repository.quanan.QuanAnDanhMucRepository;
import anuong.cantho.backend.repository.quanan.QuanAnRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuanAnDanhMucService {

    private final QuanAnDanhMucRepository quanAnDanhMucRepository;
    private final QuanAnRepository quanAnRepository;
    private final DanhMucRepository danhMucRepository;

    // Chuyển Entity thành ResponseDTO
    private QuanAnDanhMucResponseDTO toResponseDTO(
            QuanAnDanhMuc quanAnDanhMuc) {
        return new QuanAnDanhMucResponseDTO(
                quanAnDanhMuc.getId(),
                quanAnDanhMuc.getQuanAn().getId(),
                quanAnDanhMuc.getDanhMuc().getId());
    }

    // Lấy tất cả liên kết quán - danh mục
    @Transactional(readOnly = true)
    public List<QuanAnDanhMucResponseDTO> layTatCa() {
        return quanAnDanhMucRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Lấy danh mục của một quán
    @Transactional(readOnly = true)
    public List<QuanAnDanhMucResponseDTO> layTheoQuanAn(
            Long quanAnId) {
        if (!quanAnRepository.existsById(quanAnId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Không tìm thấy quán ăn có ID: " + quanAnId);
        }

        return quanAnDanhMucRepository.findByQuanAn_Id(quanAnId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Lấy các quán thuộc một danh mục
    @Transactional(readOnly = true)
    public List<QuanAnDanhMucResponseDTO> layTheoDanhMuc(
            Long danhMucId) {
        if (!danhMucRepository.existsById(danhMucId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Không tìm thấy danh mục có ID: " + danhMucId);
        }

        return quanAnDanhMucRepository.findByDanhMuc_Id(danhMucId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Thêm liên kết quán - danh mục
    @Transactional
    public QuanAnDanhMucResponseDTO them(
            QuanAnDanhMucRequestDTO request) {
        Long quanAnId = request.getQuanAnId();
        Long danhMucId = request.getDanhMucId();

        // Kiểm tra quán ăn tồn tại
        QuanAn quanAn = quanAnRepository.findById(quanAnId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy quán ăn có ID: " + quanAnId));

        if (!Boolean.TRUE.equals(quanAn.getHoatDong())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Quán đã ngưng hoạt động, không thể thêm danh mục.");
        }

        // Kiểm tra danh mục tồn tại
        DanhMuc danhMuc = danhMucRepository.findById(danhMucId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy danh mục có ID: " + danhMucId));

        // Không cho phép thêm liên kết trùng
        if (quanAnDanhMucRepository
                .existsByQuanAn_IdAndDanhMuc_Id(quanAnId, danhMucId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Quán ăn đã được gán danh mục này");
        }

        QuanAnDanhMuc entity = new QuanAnDanhMuc();
        entity.setQuanAn(quanAn);
        entity.setDanhMuc(danhMuc);

        QuanAnDanhMuc saved = quanAnDanhMucRepository.save(entity);

        return toResponseDTO(saved);
    }

    // Cập nhật liên kết
    @Transactional
    public QuanAnDanhMucResponseDTO capNhat(
            Long id,
            QuanAnDanhMucRequestDTO request) {
        QuanAnDanhMuc entity = quanAnDanhMucRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy liên kết có ID: " + id));

        Long quanAnId = request.getQuanAnId();
        Long danhMucId = request.getDanhMucId();

        QuanAn quanAn = quanAnRepository.findById(quanAnId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy quán ăn có ID: " + quanAnId));

        DanhMuc danhMuc = danhMucRepository.findById(danhMucId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy danh mục có ID: " + danhMucId));

        // Kiểm tra liên kết trùng với bản ghi khác
        if (quanAnDanhMucRepository
                .existsByQuanAn_IdAndDanhMuc_IdAndIdNot(
                        quanAnId, danhMucId, id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Quán ăn đã được gán danh mục này");
        }

        entity.setQuanAn(quanAn);
        entity.setDanhMuc(danhMuc);

        QuanAnDanhMuc saved = quanAnDanhMucRepository.save(entity);

        return toResponseDTO(saved);
    }

    // Xóa liên kết, không xóa quán hay danh mục
    @Transactional
    public void xoa(Long id) {
        QuanAnDanhMuc entity = quanAnDanhMucRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy liên kết có ID: " + id));

        quanAnDanhMucRepository.delete(entity);
    }
}
