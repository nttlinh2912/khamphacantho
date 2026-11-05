package anuong.cantho.backend.controller.quanan;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import anuong.cantho.backend.dto.quanan.hinhanhquan.HinhAnhQuanResponseDTO;
import anuong.cantho.backend.service.quanan.HinhAnhQuanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hinh-anh-quan")
// @CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = "https://didaucantho.netlify.app")
@RequiredArgsConstructor
public class HinhAnhQuanController {

  private final HinhAnhQuanService hinhAnhQuanService;

  // ======================================
  // 1. LẤY TẤT CẢ ẢNH HOẶC LỌC THEO QUÁN
  // ======================================
  // GET /api/hinh-anh-quan
  // GET /api/hinh-anh-quan?quanAnId=1
  @GetMapping
  public ResponseEntity<List<HinhAnhQuanResponseDTO>> layDanhSach(
      @RequestParam(required = false) Long quanAnId) {

    List<HinhAnhQuanResponseDTO> danhSach;

    if (quanAnId != null) {
      danhSach = hinhAnhQuanService.layTheoQuanAn(quanAnId);
    } else {
      danhSach = hinhAnhQuanService.layTatCa();
    }

    return ResponseEntity.ok(danhSach);
  }

  // ======================================
  // 2. TẢI LÊN MỘT HOẶC NHIỀU ẢNH
  // ======================================
  // POST /api/hinh-anh-quan/upload?quanAnId=1
  // Content-Type: multipart/form-data
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<List<HinhAnhQuanResponseDTO>> taiLen(
      @RequestParam Long quanAnId,
      @RequestParam("files") MultipartFile[] files,
      @RequestParam(required = false) String chuThich) {

    List<HinhAnhQuanResponseDTO> ketQua = hinhAnhQuanService.taiLen(
        quanAnId,
        files,
        chuThich);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ketQua);
  }

  // ======================================
  // 3. CHỌN ẢNH ĐẠI DIỆN
  // ======================================
  // PATCH /api/hinh-anh-quan/1/anh-dai-dien
  @PatchMapping("/{id}/anh-dai-dien")
  public ResponseEntity<HinhAnhQuanResponseDTO> datAnhDaiDien(
      @PathVariable Long id) {

    HinhAnhQuanResponseDTO ketQua = hinhAnhQuanService.datAnhDaiDien(id);

    return ResponseEntity.ok(ketQua);
  }

  // ======================================
  // 4. CẬP NHẬT THỨ TỰ HIỂN THỊ
  // ======================================
  // PATCH /api/hinh-anh-quan/1/thu-tu
  // Body: {"thuTu": 2}
  @PatchMapping("/{id}/thu-tu")
  public ResponseEntity<HinhAnhQuanResponseDTO> capNhatThuTu(
      @PathVariable Long id,
      @RequestBody Map<String, Integer> request) {

    Integer thuTu = request.get("thuTu");

    HinhAnhQuanResponseDTO ketQua = hinhAnhQuanService.capNhatThuTu(id, thuTu);

    return ResponseEntity.ok(ketQua);
  }

  // ======================================
  // 5. XÓA HÌNH ẢNH
  // ======================================
  // DELETE /api/hinh-anh-quan/1
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> xoa(@PathVariable Long id) {

    hinhAnhQuanService.xoa(id);

    return ResponseEntity.noContent().build();
  }
}
