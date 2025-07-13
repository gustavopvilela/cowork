package backend.coworking.dto;

import backend.coworking.entity.Reserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ReservaDTO extends RepresentationModel<ReservaDTO> {

    @EqualsAndHashCode.Include
    private Long id;
    private Instant entrada;
    private Instant saida;
    private UsuarioDTO usuario;
    private EspacoDTO espaco;
    private ServicoDTO servico;

    public ReservaDTO(Reserva entity) {
        this.id = entity.getId();
        this.entrada = entity.getEntrada();
        this.saida = entity.getSaida();
        this.usuario = new UsuarioDTO(entity.getUsuario());
        this.espaco = new EspacoDTO(entity.getEspaco());
        if (entity.getServico() != null) {
            this.servico = new ServicoDTO(entity.getServico());
        }
    }
}