package anuong.cantho.backend.service.baiviet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import anuong.cantho.backend.entity.baiviet.BaiViet;
import anuong.cantho.backend.entity.baiviet.HinhAnhBaiViet;
import anuong.cantho.backend.repository.baiviet.BaiVietRepository;
import anuong.cantho.backend.repository.baiviet.HinhAnhBaiVietRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class SchemaBaiVietService {

  private final BaiVietRepository baiVietRepository;
    private final HinhAnhBaiVietRepository hinhAnhRepository;

    @Value ("${app.seo.site-url}")
    private String siteUrl;

    @Value("${app.seo.site-name}")
    private String siteName;

    @Value("${app.seo.logo-url}")
    private String logoUrl;

    @Value("${app.seo.article-path:/bai-viet/}")
    private String articlePath;

    // =====================================================
    // TẠO SCHEMA BÀI VIẾT
    // =====================================================

    @Transactional(readOnly = true)
    public Map<String, Object> taoSchema(
            String slug,
            String loaiSchema
    ) {

        BaiViet baiViet = baiVietRepository
                .timBaiVietDaXuatBanTheoSlug(
                        slug,
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy bài viết đã xuất bản"
                ));

        List<HinhAnhBaiViet> danhSachAnh =
                hinhAnhRepository
                        .findByBaiViet_IdOrderByThuTuAscIdAsc(
                                baiViet.getId()
                        );

        String urlBaiViet = ghepUrl(
                siteUrl,
                articlePath + baiViet.getDuongDanSeo()
        );

        String tieuDeSeo = coNoiDung(baiViet.getTieuDeSeo())
                ? baiViet.getTieuDeSeo()
                : baiViet.getTieuDe();

        String moTaSeo = coNoiDung(baiViet.getMoTaSeo())
                ? baiViet.getMoTaSeo()
                : baiViet.getTomTat();

        // =================================================
        // TẠO DANH SÁCH ẢNH
        // =================================================

        List<String> danhSachUrlAnh = new ArrayList<>();

        if (coNoiDung(baiViet.getHinhDaiDien())) {
            danhSachUrlAnh.add(
                    chuanHoaUrl(baiViet.getHinhDaiDien())
            );
        }

        for (HinhAnhBaiViet anh : danhSachAnh) {

            if (!coNoiDung(anh.getUrlAnh())) {
                continue;
            }

            String urlAnh = chuanHoaUrl(anh.getUrlAnh());

            if (!danhSachUrlAnh.contains(urlAnh)) {
                danhSachUrlAnh.add(urlAnh);
            }
        }

        // =================================================
        // CHỌN LOẠI SCHEMA
        // =================================================

        String loai = "NEWSARTICLE".equalsIgnoreCase(loaiSchema)
                ? "NewsArticle"
                : "Article";

        // =================================================
        // TẠO ARTICLE / NEWSARTICLE
        // =================================================

        Map<String, Object> schema = new LinkedHashMap<>();

        schema.put("@context", "https://schema.org");
        schema.put("@type", loai);

        schema.put("@id", urlBaiViet + "#article");

        schema.put("mainEntityOfPage", Map.of(
                "@type", "WebPage",
                "@id", urlBaiViet
        ));

        schema.put("headline", tieuDeSeo);

        if (coNoiDung(moTaSeo)) {
            schema.put("description", moTaSeo);
        }

        if (!danhSachUrlAnh.isEmpty()) {
            schema.put("image", danhSachUrlAnh);
        }

        schema.put("datePublished",
                dinhDangNgay(baiViet.getNgayDang()));

        schema.put("dateModified",
                dinhDangNgay(baiViet.getNgayCapNhat()));

        schema.put("inLanguage", "vi-VN");

        schema.put("articleSection",
                baiViet.getDanhMucBaiViet().getTenDanhMuc());

        if (coNoiDung(baiViet.getTuKhoaChinh())) {
            schema.put("keywords", baiViet.getTuKhoaChinh());
        } else if (coNoiDung(baiViet.getTuKhoaSeo())) {
            schema.put("keywords", baiViet.getTuKhoaSeo());
        }

        // =================================================
        // TÁC GIẢ
        // =================================================

        schema.put("author", Map.of(
                "@type", "Organization",
                "name", siteName,
                "url", siteUrl
        ));

        // =================================================
        // NHÀ XUẤT BẢN
        // =================================================

        Map<String, Object> logo = new LinkedHashMap<>();

        logo.put("@type", "ImageObject");
        logo.put("url", chuanHoaUrl(logoUrl));

        Map<String, Object> publisher = new LinkedHashMap<>();

        publisher.put("@type", "Organization");
        publisher.put("name", siteName);
        publisher.put("url", siteUrl);
        publisher.put("logo", logo);

        schema.put("publisher", publisher);

        // =================================================
        // TRẢ KẾT QUẢ
        // =================================================

        return schema;
    }

    // =====================================================
    // TẠO BREADCRUMB SCHEMA
    // =====================================================

    @Transactional (readOnly = true)
    public Map<String, Object> taoBreadcrumb(
            String slug
    ) {

        BaiViet baiViet = baiVietRepository
                .timBaiVietDaXuatBanTheoSlug(
                        slug,
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy bài viết đã xuất bản"
                ));

        String urlBaiViet = ghepUrl(
                siteUrl,
                articlePath + baiViet.getDuongDanSeo()
        );

        String slugDanhMuc =
                baiViet.getDanhMucBaiViet().getDuongDanSeo();

        String urlDanhMuc = ghepUrl(
                siteUrl,
                "/danh-muc-bai-viet/" + slugDanhMuc
        );

        List<Map<String, Object>> items = new ArrayList<>();

        items.add(taoBreadcrumbItem(
                1,
                "Trang chủ",
                ghepUrl(siteUrl, "/")
        ));

        items.add(taoBreadcrumbItem(
                2,
                baiViet.getDanhMucBaiViet().getTenDanhMuc(),
                urlDanhMuc
        ));

        items.add(taoBreadcrumbItem(
                3,
                baiViet.getTieuDe(),
                urlBaiViet
        ));

        Map<String, Object> schema = new LinkedHashMap<>();

        schema.put("@context", "https://schema.org");
        schema.put("@type", "BreadcrumbList");
        schema.put("itemListElement", items);

        return schema;
    }

    private Map<String, Object> taoBreadcrumbItem(
            int position,
            String name,
            String url
    ) {

        Map<String, Object> item = new LinkedHashMap<>();

        item.put("@type", "ListItem");
        item.put("position", position);
        item.put("name", name);
        item.put("item", url);

        return item;
    }

    // =====================================================
    // HÀM HỖ TRỢ
    // =====================================================

    private boolean coNoiDung(String value) {
        return value != null && !value.isBlank();
    }

    private String dinhDangNgay(LocalDateTime ngay) {
        return ngay == null ? null : ngay.toString();
    }

    private String ghepUrl(String base, String path) {
        return base.replaceAll("/+$", "")
                + "/"
                + path.replaceAll("^/+", "");
    }

    private String chuanHoaUrl(String url) {

        if (url == null || url.isBlank()) {
            return "";
        }

        String value = url.trim();

        if (value.startsWith("http://")
                || value.startsWith("https://")) {
            return value;
        }

        return ghepUrl(siteUrl, value);
    }

}
