package anuong.cantho.backend.controller.baiviet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.dto.baiviet.baiviet.seo.PhanTichSeoRequestDTO;
import anuong.cantho.backend.dto.baiviet.baiviet.seo.PhanTichSeoResponseDTO;
import anuong.cantho.backend.service.baiviet.SeoBaiVietService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/bai-viet/seo")
@RequiredArgsConstructor 
// @CrossOrigin (origins = "http://localhost:5173")
@CrossOrigin(origins = "https://didaucantho.netlify.app")
public class SeoBaiVietController {

  private final SeoBaiVietService seoBaiVietService;

    // Phân tích SEO nội dung đang soạn, kể cả khi chưa lưu bài viết
    @PostMapping("/phan-tich")
    public ResponseEntity<PhanTichSeoResponseDTO> phanTich(
            @Valid @RequestBody PhanTichSeoRequestDTO dto
    ) {

        return ResponseEntity.ok(
                seoBaiVietService.phanTich(dto)
        );
    }

}
