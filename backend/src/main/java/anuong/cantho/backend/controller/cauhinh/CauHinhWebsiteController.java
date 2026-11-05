package anuong.cantho.backend.controller.cauhinh;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import anuong.cantho.backend.dto.cauhinh.CauHinhWebsiteRequestDTO;
import anuong.cantho.backend.dto.cauhinh.CauHinhWebsiteResponseDTO;
import anuong.cantho.backend.service.cauhinh.CauHinhWebsiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cau-hinh-website")
@RequiredArgsConstructor
// @CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = "https://didaucantho.netlify.app")
@Validated
public class CauHinhWebsiteController {

    private final CauHinhWebsiteService service;

    // ==========================================
    // LẤY CẤU HÌNH WEBSITE
    // GET /api/cau-hinh-website
    // ==========================================

    @GetMapping
    public ResponseEntity<CauHinhWebsiteResponseDTO> layCauHinh() {

        return ResponseEntity.ok(
                service.layCauHinh());
    }

    // ==========================================
    // CẬP NHẬT CẤU HÌNH
    // PUT /api/cau-hinh-website
    // ==========================================

    @PutMapping
    public ResponseEntity<CauHinhWebsiteResponseDTO> capNhatCauHinh(
            @Valid @RequestBody CauHinhWebsiteRequestDTO request) {

        return ResponseEntity.ok(
                service.capNhatCauHinh(request));
    }

    // ==========================================
    // UPLOAD LOGO
    // POST /api/cau-hinh-website/upload/logo
    // ==========================================

    @PostMapping(value = "/upload/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CauHinhWebsiteResponseDTO> uploadLogo(
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                service.uploadLogo(file));
    }

    // ==========================================
    // UPLOAD FAVICON
    // POST /api/cau-hinh-website/upload/favicon
    // ==========================================

    @PostMapping(value = "/upload/favicon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CauHinhWebsiteResponseDTO> uploadFavicon(
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                service.uploadFavicon(file));
    }

    // ==========================================
    // UPLOAD BANNER TRANG CHỦ
    // POST /api/cau-hinh-website/upload/banner
    // ==========================================

    @PostMapping(value = "/upload/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CauHinhWebsiteResponseDTO> uploadBanner(
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                service.uploadBanner(file));
    }

}
