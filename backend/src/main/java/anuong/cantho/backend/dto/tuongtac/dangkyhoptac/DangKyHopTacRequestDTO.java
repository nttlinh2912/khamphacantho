package anuong.cantho.backend.dto.tuongtac.dangkyhoptac;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class DangKyHopTacRequestDTO {

  @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
    private String hoTen;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern (
        regexp = "^(0[35789])[0-9]{8}$",
        message = "Số điện thoại không đúng định dạng"
    )
    private String soDienThoai;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 254, message = "Email không được vượt quá 254 ký tự")
    private String email;

    @NotBlank(message = "Tên quán không được để trống")
    @Size(max = 200, message = "Tên quán không được vượt quá 200 ký tự")
    private String tenQuan;

    @NotBlank(message = "Địa chỉ quán không được để trống")
    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String diaChiQuan;

    private String noiDung;

}
