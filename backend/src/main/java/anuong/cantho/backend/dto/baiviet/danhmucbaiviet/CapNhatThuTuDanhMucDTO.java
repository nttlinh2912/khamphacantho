package anuong.cantho.backend.dto.baiviet.danhmucbaiviet;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CapNhatThuTuDanhMucDTO(
    @NotEmpty(message = "Danh sách cập nhật không được để trống") @Size(max = 500, message = "Chỉ được cập nhật tối đa 500 danh mục mỗi lần") List<@Valid ChiTietThuTu> danhSach) {
  public record ChiTietThuTu(

      @NotNull(message = "ID danh mục không được để trống") Long id,

      @NotNull(message = "Thứ tự không được để trống") @PositiveOrZero(message = "Thứ tự không được âm") Integer thuTu

  ) {
  }
}
