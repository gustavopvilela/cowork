package backend.coworking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EspacoStatsDTO {
    private EspacoDTO espaco;
    private Long qtde_notas_1_estrela;
    private Long qtde_notas_2_estrela;
    private Long qtde_notas_3_estrela;
    private Long qtde_notas_4_estrela;
    private Long qtde_notas_5_estrela;
    private BigDecimal media;
}