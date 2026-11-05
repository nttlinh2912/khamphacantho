package anuong.cantho.backend.dto.baiviet.danhmucbaiviet;

public record DanhMucBaiVietResponseDTO(
    Long id,
    String tenDanhMuc,
    String duongDanSeo,
    String moTa,
    Integer thuTu,
    Boolean hoatDong) {

}
