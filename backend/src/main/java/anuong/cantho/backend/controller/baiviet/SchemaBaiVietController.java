package anuong.cantho.backend.controller.baiviet;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import anuong.cantho.backend.service.baiviet.SchemaBaiVietService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bai-viet/seo/schema")
@RequiredArgsConstructor
// @CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = "https://didaucantho.netlify.app")
public class SchemaBaiVietController {

  private final SchemaBaiVietService schemaService;

  // ==========================================
  // 1. LẤY SCHEMA ARTICLE HOẶC NEWSARTICLE
  // ==========================================

  @GetMapping("/{slug}")
  public ResponseEntity<Map<String, Object>> getSchemaBaiViet(

      @PathVariable String slug,

      @RequestParam(defaultValue = "ARTICLE") String loai

  ) {

    return ResponseEntity.ok(
        schemaService.taoSchema(slug, loai));
  }

  // ==========================================
  // 2. LẤY SCHEMA BREADCRUMB
  // ==========================================

  @GetMapping("/{slug}/breadcrumb")
  public ResponseEntity<Map<String, Object>> getSchemaBreadcrumb(

      @PathVariable String slug

  ) {

    return ResponseEntity.ok(
        schemaService.taoBreadcrumb(slug));
  }

}
