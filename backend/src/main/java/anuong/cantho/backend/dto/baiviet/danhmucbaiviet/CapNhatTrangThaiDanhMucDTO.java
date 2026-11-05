package anuong.cantho.backend.dto.baiviet.danhmucbaiviet;

import jakarta.validation.constraints.NotNull;

public record CapNhatTrangThaiDanhMucDTO(
    @NotNull(message = "Trạng thái không được để trống") Boolean hoatDong) {

}
