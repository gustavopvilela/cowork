package backend.coworking.dto;

import backend.coworking.entity.Avaliacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AvaliacaoDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private int nota;
    private String comentario;
    private Instant dataAvaliacao;
    private UsuarioDTO autor;

    public AvaliacaoDTO(Avaliacao entity) {
        this.id = entity.getId();
        this.nota = entity.getNota();
        this.comentario = entity.getComentario();
        this.dataAvaliacao = entity.getDataAvaliacao();
        this.autor = new UsuarioDTO(entity.getAutor());
    }
}
