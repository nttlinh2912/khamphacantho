package anuong.cantho.backend.service.baiviet;

import java.net.URI;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import anuong.cantho.backend.dto.baiviet.baiviet.seo.KetQuaTieuChiSeoDTO;
import anuong.cantho.backend.dto.baiviet.baiviet.seo.PhanTichSeoRequestDTO;
import anuong.cantho.backend.dto.baiviet.baiviet.seo.PhanTichSeoResponseDTO;

@Service
public class SeoBaiVietService {

  @Value("${app.seo.base-url:http://localhost:5173}")
  private String baseUrl;

  private static final Pattern KHOANG_TRANG = Pattern.compile("\\s+");

  // =====================================================
  // PHÂN TÍCH SEO
  // =====================================================

  public PhanTichSeoResponseDTO phanTich(
      PhanTichSeoRequestDTO dto) {

    List<KetQuaTieuChiSeoDTO> ketQua = new ArrayList<>();

    String tuKhoa = lamSach(dto.getTuKhoaChinh());
    String tieuDe = lamSach(dto.getTieuDe());
    String slug = lamSach(dto.getDuongDanSeo());
    String tieuDeSeo = lamSach(dto.getTieuDeSeo());
    String moTaSeo = lamSach(dto.getMoTaSeo());
    String tomTat = lamSach(dto.getTomTat());

    String noiDungHtml = dto.getNoiDung() == null
        ? ""
        : dto.getNoiDung();

    // Phân tích HTML
    Document document = Jsoup.parse(noiDungHtml);

    // Loại bỏ các thành phần không phải nội dung văn bản
    document.select("script, style, noscript").remove();

    String noiDungText = document.text();

    int soTu = demTu(noiDungText);

    // Chuẩn hóa từ khóa
    String tuKhoaChuan = chuanHoa(tuKhoa);
    String noiDungChuan = chuanHoa(noiDungText);

    int soLanXuatHien = demSoLanXuatHien(
        noiDungChuan,
        tuKhoaChuan);

    double matDoTuKhoa = soTu == 0 || tuKhoa.isBlank()
        ? 0
        : (double) soLanXuatHien / soTu * 100;

    // Lấy đoạn văn đầu tiên
    String doanDauTien = layDoanDauTien(document);

    // =================================================
    // A. KIỂM TRA TỪ KHÓA
    // =================================================

    boolean coTuKhoa = !tuKhoa.isBlank();

    themKetQua(
        ketQua,
        "CO_TU_KHOA_CHINH",
        "Đặt từ khóa tập trung cho nội dung này",
        coTuKhoa,
        coTuKhoa
            ? "Đã nhập từ khóa chính"
            : "Bạn chưa nhập từ khóa chính");

    themKetQua(
        ketQua,
        "TU_KHOA_TRONG_TIEU_DE",
        "Thêm từ khóa chính vào tiêu đề SEO",
        coChua(tieuDeSeo, tuKhoa),
        coChua(tieuDeSeo, tuKhoa)
            ? "Tiêu đề SEO đã có từ khóa chính"
            : "Hãy thêm từ khóa chính vào tiêu đề SEO");

    themKetQua(
        ketQua,
        "TU_KHOA_DAU_TIEU_DE",
        "Sử dụng từ khóa chính gần đầu tiêu đề SEO",
        namGanDau(tieuDeSeo, tuKhoa, 30),
        "Nên đặt từ khóa chính ở phần đầu tiêu đề SEO");

    themKetQua(
        ketQua,
        "TU_KHOA_TRONG_URL",
        "Sử dụng từ khóa chính trong URL",
        coChua(chuanHoaSlug(slug), chuanHoaSlug(tuKhoa)),
        "Nên đưa từ khóa chính vào đường dẫn SEO");

    themKetQua(
        ketQua,
        "TU_KHOA_DAU_NOI_DUNG",
        "Sử dụng từ khóa chính ở đầu nội dung",
        coChua(doanDauTien, tuKhoa),
        "Nên xuất hiện từ khóa chính trong đoạn mở đầu");

    themKetQua(
        ketQua,
        "TU_KHOA_TRONG_NOI_DUNG",
        "Sử dụng từ khóa chính trong nội dung",
        soLanXuatHien > 0,
        "Hãy sử dụng từ khóa chính tự nhiên trong nội dung");

    themKetQua(
        ketQua,
        "MAT_DO_TU_KHOA",
        "Mật độ từ khóa khoảng 1%",
        matDoTuKhoa >= 0.5 && matDoTuKhoa <= 2.0,
        "Mật độ từ khóa hiện tại: "
            + String.format(Locale.US, "%.2f", matDoTuKhoa)
            + "%. Mức tham khảo là 0,5%-2%");

    // =================================================
    // B. TIÊU ĐỀ VÀ MÔ TẢ SEO
    // =================================================

    int doDaiTieuDeSeo = tieuDeSeo.length();

    themKetQua(
        ketQua,
        "DO_DAI_TIEU_DE_SEO",
        "Tiêu đề SEO nên có khoảng 50-60 ký tự",
        doDaiTieuDeSeo >= 30 && doDaiTieuDeSeo <= 60,
        "Tiêu đề SEO hiện có "
            + doDaiTieuDeSeo
            + " ký tự. Nên hướng đến khoảng 50-60 ký tự");

    themKetQua(
        ketQua,
        "MO_TA_SEO_TON_TAI",
        "Thêm từ khóa tập trung vào mô tả Meta SEO",
        !moTaSeo.isBlank() && coChua(moTaSeo, tuKhoa),
        "Meta Description cần có từ khóa chính");

    themKetQua(
        ketQua,
        "DO_DAI_MO_TA_SEO",
        "Mô tả Meta SEO nên có khoảng 120-160 ký tự",
        moTaSeo.length() >= 120 && moTaSeo.length() <= 160,
        "Mô tả SEO hiện có "
            + moTaSeo.length()
            + " ký tự. Nên hướng đến khoảng 120-160 ký tự");

    // =================================================
    // C. NỘI DUNG
    // =================================================

    themKetQua(
        ketQua,
        "DO_DAI_NOI_DUNG",
        "Nội dung bài viết nên có khoảng 600-2500 từ",
        soTu >= 600 && soTu <= 2500,
        "Nội dung hiện có "
            + soTu
            + " từ. Mức tham khảo là 600-2500 từ");

    boolean coH2H3 = !document.select("h2, h3").isEmpty();

    themKetQua(
        ketQua,
        "CO_TIEU_DE_PHU",
        "Sử dụng tiêu đề phụ H2 hoặc H3",
        coH2H3,
        "Nên chia nội dung thành các phần bằng tiêu đề H2 và H3");

    themKetQua(
        ketQua,
        "TU_KHOA_TRONG_TIEU_DE_PHU",
        "Sử dụng từ khóa chính trong tiêu đề phụ",
        coTieuDePhuChuaTuKhoa(document, tuKhoa),
        "Nên có tiêu đề H2 hoặc H3 chứa từ khóa chính hoặc biến thể phù hợp");

    themKetQua(
        ketQua,
        "CO_TIEU_DE",
        "Bài viết có tiêu đề",
        !tieuDe.isBlank(),
        "Hãy nhập tiêu đề cho bài viết");

    themKetQua(
        ketQua,
        "TU_KHOA_TRONG_TIEU_DE",
        "Sử dụng từ khóa chính trong tiêu đề bài viết",
        coChua(tieuDe, tuKhoa),
        "Nên thêm từ khóa chính vào tiêu đề bài viết");

    themKetQua(
        ketQua,
        "CO_TOM_TAT",
        "Bài viết có phần tóm tắt",
        !tomTat.isBlank(),
        "Nên bổ sung phần tóm tắt giới thiệu nội dung bài viết");

    themKetQua(
        ketQua,
        "TU_KHOA_TRONG_TOM_TAT",
        "Sử dụng từ khóa chính trong phần tóm tắt",
        coChua(tomTat, tuKhoa),
        "Nên đưa từ khóa chính vào phần tóm tắt một cách tự nhiên");

    int soDoan = document.select("p").size();

    themKetQua(
        ketQua,
        "DOAN_VAN",
        "Chia nội dung thành các đoạn văn dễ đọc",
        soDoan >= 3,
        "Nên chia bài viết thành các đoạn văn ngắn, rõ ý");

    // =================================================
    // D. HÌNH ẢNH
    // =================================================

    boolean coAnhDaiDien = !lamSach(dto.getHinhDaiDien()).isBlank();

    themKetQua(
        ketQua,
        "CO_ANH_DAI_DIEN",
        "Bài viết có hình ảnh đại diện",
        coAnhDaiDien,
        "Bài viết cần có ảnh đại diện");

    String altDaiDien = lamSach(dto.getHinhDaiDienAlt());

    themKetQua(
        ketQua,
        "ALT_ANH_DAI_DIEN",
        "Thêm từ khóa hoặc mô tả phù hợp vào ALT ảnh đại diện",
        coAnhDaiDien && !altDaiDien.isBlank(),
        "Hãy nhập ALT mô tả chính xác nội dung ảnh đại diện");

    List<PhanTichSeoRequestDTO.HinhAnhSeoDTO> danhSachAnh = dto.getDanhSachHinhAnh() == null
        ? List.of()
        : dto.getDanhSachHinhAnh();

    boolean coAnhNoiDung = !danhSachAnh.isEmpty();

    themKetQua(
        ketQua,
        "CO_ANH_NOI_DUNG",
        "Thêm hình ảnh vào nội dung bài viết",
        coAnhNoiDung,
        "Nên bổ sung hình ảnh minh họa cho bài viết");

    boolean tatCaAnhCoAlt = coAnhNoiDung
        && danhSachAnh.stream().allMatch(
            anh -> anh.getAltText() != null
                && !anh.getAltText().isBlank());

    themKetQua(
        ketQua,
        "ALT_ANH_NOI_DUNG",
        "Thêm ALT cho hình ảnh nội dung",
        tatCaAnhCoAlt,
        "Hãy bổ sung ALT mô tả cho tất cả ảnh trong thư viện");

    // =================================================
    // E. LIÊN KẾT
    // =================================================

    Elements links = document.select("a[href]");

    boolean coLienKetNoiBo = false;
    boolean coLienKetNgoai = false;

    for (Element link : links) {

      String href = link.attr("href").trim();

      if (href.isBlank()) {
        continue;
      }

      if (laLienKetNoiBo(href)) {
        coLienKetNoiBo = true;
      } else if (href.startsWith("http://")
          || href.startsWith("https://")) {
        coLienKetNgoai = true;
      }
    }

    themKetQua(
        ketQua,
        "LIEN_KET_NOI_BO",
        "Thêm liên kết nội bộ vào nội dung",
        coLienKetNoiBo,
        "Nên liên kết đến các bài viết hoặc trang liên quan trên website");

    themKetQua(
        ketQua,
        "LIEN_KET_NGOAI",
        "Thêm liên kết ngoài có liên quan",
        coLienKetNgoai,
        "Có thể bổ sung liên kết đến nguồn tham khảo đáng tin cậy");

    // =================================================
    // TỔNG HỢP ĐIỂM
    // =================================================

    int tongTieuChi = ketQua.size();

    int tieuChiDat = (int) ketQua.stream()
        .filter(KetQuaTieuChiSeoDTO::isDat)
        .count();

    int tieuChiChuaDat = tongTieuChi - tieuChiDat;

    int tongDiem = tongTieuChi == 0
        ? 0
        : (int) Math.round(
            (double) tieuChiDat / tongTieuChi * 100);

    return new PhanTichSeoResponseDTO(
        tongDiem,
        tongTieuChi,
        tieuChiDat,
        tieuChiChuaDat,
        soTu,
        Math.round(matDoTuKhoa * 100.0) / 100.0,
        ketQua);
  }

  // =====================================================
  // HÀM HỖ TRỢ
  // =====================================================

  private void themKetQua(
      List<KetQuaTieuChiSeoDTO> danhSach,
      String ma,
      String ten,
      boolean dat,
      String thongBao) {
    danhSach.add(
        new KetQuaTieuChiSeoDTO(
            ma,
            ten,
            dat,
            thongBao));
  }

  private String lamSach(String value) {
    return value == null ? "" : value.trim();
  }

  private int demTu(String text) {
    if (text == null || text.isBlank()) {
      return 0;
    }

    return text.trim().split(KHOANG_TRANG.pattern()).length;
  }

  private String chuanHoa(String text) {
    return lamSach(text)
        .toLowerCase(Locale.ROOT)
        .replaceAll("[^\\p{L}\\p{N}]+", " ")
        .trim()
        .replaceAll("\\s+", " ");
  }

  private String chuanHoaSlug(String text) {
    String normalized = Normalizer.normalize(
        lamSach(text).toLowerCase(Locale.ROOT),
        Normalizer.Form.NFD);

    normalized = normalized.replaceAll("\\p{M}", "");

    return normalized
        .replace('đ', 'd')
        .replaceAll("[^a-z0-9]+", "-")
        .replaceAll("^-|-$", "");
  }

  private boolean coChua(String noiDung, String tuKhoa) {
    if (noiDung == null || tuKhoa == null
        || noiDung.isBlank() || tuKhoa.isBlank()) {
      return false;
    }

    return chuanHoa(noiDung).contains(chuanHoa(tuKhoa));
  }

  private int demSoLanXuatHien(
      String noiDung,
      String tuKhoa) {
    if (noiDung == null || tuKhoa == null
        || noiDung.isBlank() || tuKhoa.isBlank()) {
      return 0;
    }

    String[] tuNoiDung = noiDung.split("\\s+");
    String[] tuKhoaMang = tuKhoa.split("\\s+");

    int dem = 0;

    for (int i = 0; i <= tuNoiDung.length - tuKhoaMang.length; i++) {

      boolean khop = true;

      for (int j = 0; j < tuKhoaMang.length; j++) {
        if (!tuNoiDung[i + j].equals(tuKhoaMang[j])) {
          khop = false;
          break;
        }
      }

      if (khop) {
        dem++;
      }
    }

    return dem;
  }

  private boolean namGanDau(
      String tieuDe,
      String tuKhoa,
      int gioiHanKyTu) {
    if (tieuDe.isBlank() || tuKhoa.isBlank()) {
      return false;
    }

    String title = chuanHoa(tieuDe);
    String keyword = chuanHoa(tuKhoa);

    int viTri = title.indexOf(keyword);

    return viTri >= 0 && viTri <= gioiHanKyTu;
  }

  private String layDoanDauTien(Document document) {
    Element p = document.selectFirst("p");

    if (p != null) {
      return p.text();
    }

    return document.text();
  }

  private boolean coTieuDePhuChuaTuKhoa(
      Document document,
      String tuKhoa) {
    if (tuKhoa.isBlank()) {
      return false;
    }

    return document.select("h2, h3, h4")
        .stream()
        .anyMatch(element -> coChua(element.text(), tuKhoa));
  }

  private boolean laLienKetNoiBo(String href) {

    if (href.startsWith("/") && !href.startsWith("//")) {
      return true;
    }

    try {
      URI linkUri = URI.create(href);
      URI baseUri = URI.create(baseUrl);

      return linkUri.getHost() != null
          && baseUri.getHost() != null
          && linkUri.getHost().equalsIgnoreCase(baseUri.getHost());

    } catch (IllegalArgumentException ex) {
      return false;
    }
  }
}
