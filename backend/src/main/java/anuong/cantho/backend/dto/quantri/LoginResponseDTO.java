package anuong.cantho.backend.dto.quantri;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor  
public class LoginResponseDTO {

  private String token;
  private String tenDangNhap;
  private String hoTen;
}
