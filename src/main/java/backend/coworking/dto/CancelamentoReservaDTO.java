package backend.coworking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelamentoReservaDTO {
    private Instant inicio;
    private Instant fim;
    private String motivo;
}
