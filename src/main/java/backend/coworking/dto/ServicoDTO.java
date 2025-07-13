package backend.coworking.dto;

import backend.coworking.entity.Servico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ServicoDTO extends RepresentationModel<ServicoDTO> {

    @EqualsAndHashCode.Include
    private Long id;
    private String nome;
    private String descricao;

    public ServicoDTO(Servico entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.descricao = entity.getDescricao();
    }
}