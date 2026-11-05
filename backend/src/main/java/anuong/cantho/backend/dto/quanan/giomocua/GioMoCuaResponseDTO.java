package anuong.cantho.backend.dto.quanan.giomocua;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class GioMoCuaResponseDTO {

   private Long id;

    private Long quanAnId;

    private String tenQuan;

    private Integer thu;

    private LocalTime gioMo;

    private LocalTime gioDong;

    private Boolean nghi;

    private String ghiChu;

}
