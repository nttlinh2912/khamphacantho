package anuong.cantho.backend.dto.tuongtac.dangkyhoptac;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class CapNhatGhiChuHopTacDTO {

  @Size(max = 2000, message = "Ghi chú không được vượt quá 2000 ký tự")
    private String ghiChuQuanTri;

}
