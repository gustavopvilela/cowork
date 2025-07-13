package backend.coworking.dto.insert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaInsertDTO {
    private Instant entrada;
    private Instant saida;
    private Long usuarioId;
    private Long espacoId;
    private Long servicoId;
}