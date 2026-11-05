package anuong.cantho.backend.controller.diachi;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.diachi.PhuongXaRequestDTO;
import anuong.cantho.backend.dto.diachi.PhuongXaResponseDTO;
import anuong.cantho.backend.service.diachi.PhuongXaService;
import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/api/phuong-xa")
@CrossOrigin (origins = "http://localhost:5173")
public class PhuongXaController {


  private final PhuongXaService phuongXaService;

    public PhuongXaController(PhuongXaService phuongXaService) {
        this.phuongXaService = phuongXaService;
    }

    // =====================================================
    // 1. DANH SÁCH PHÂN TRANG, TÌM KIẾM VÀ LỌC
    // =====================================================
    @GetMapping
    public ResponseEntity<Page<PhuongXaResponseDTO>> search(

        @RequestParam(required = false)
        String keyword,

        @RequestParam(required = false)
        Long tinhThanhId,

        @RequestParam(required = false)
        Boolean hoatDong,

        @RequestParam(defaultValue = "0")
        int page,

        @RequestParam(defaultValue = "12")
        int size,

        @RequestParam(defaultValue = "tenPhuongXa")
        String sortBy,

        @RequestParam(defaultValue = "asc")
        String sortDir

    ) {

        Page<PhuongXaResponseDTO> result = phuongXaService.search(
            keyword,
            tinhThanhId,
            hoatDong,
            page,
            size,
            sortBy,
            sortDir
        );

        return ResponseEntity.ok(result);
    }

    // =====================================================
    // 2. LẤY TOÀN BỘ PHƯỜNG/XÃ
    // =====================================================
    @GetMapping("/all")
    public ResponseEntity<List<PhuongXaResponseDTO>> getAll() {

        return ResponseEntity.ok(
            phuongXaService.getAll()
        );
    }

    // =====================================================
    // 3. LẤY DANH SÁCH THEO TỈNH/THÀNH
    // =====================================================
    @GetMapping("/tinh-thanh/{tinhThanhId}")
    public ResponseEntity<List<PhuongXaResponseDTO>> getByTinhThanhId(
        @PathVariable Long tinhThanhId
    ) {

        return ResponseEntity.ok(
            phuongXaService.getByTinhThanhId(tinhThanhId)
        );
    }

    // =====================================================
    // 4. LẤY CHI TIẾT MỘT PHƯỜNG/XÃ
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<PhuongXaResponseDTO> getById(
        @PathVariable Long id
    ) {

        return ResponseEntity.ok(
            phuongXaService.getById(id)
        );
    }

    // =====================================================
    // 5. THÊM PHƯỜNG/XÃ
    // =====================================================
    @PostMapping
    public ResponseEntity<PhuongXaResponseDTO> create(
        @Valid @RequestBody PhuongXaRequestDTO request
    ) {

        PhuongXaResponseDTO result = phuongXaService.create(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }

    // =====================================================
    // 6. CẬP NHẬT PHƯỜNG/XÃ
    // =====================================================
    @PutMapping("/{id}")
    public ResponseEntity<PhuongXaResponseDTO> update(
        @PathVariable Long id,
        @Valid @RequestBody PhuongXaRequestDTO request
    ) {

        return ResponseEntity.ok(
            phuongXaService.update(id, request)
        );
    }

    // =====================================================
    // 7. XÓA PHƯỜNG/XÃ
    // =====================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id
    ) {

        phuongXaService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
