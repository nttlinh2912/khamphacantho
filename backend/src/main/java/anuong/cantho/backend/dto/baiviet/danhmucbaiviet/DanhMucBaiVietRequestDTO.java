package anuong.cantho.backend.dto.baiviet.danhmucbaiviet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record DanhMucBaiVietRequestDTO(
    @NotBlank(message = "Tên danh mục không được để trống") @Size(max = 255, message = "Tên danh mục không được vượt quá 255 ký tự") String tenDanhMuc,

    @NotBlank(message = "Đường dẫn SEO không được để trống") @Size(max = 255, message = "Đường dẫn SEO không được vượt quá 255 ký tự") String duongDanSeo,

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự") String moTa,

    @PositiveOrZero(message = "Thứ tự không được âm") Integer thuTu,

    Boolean hoatDong) {
}
