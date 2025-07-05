package backend.coworking.dto;

import backend.coworking.constant.EspacoType;
import backend.coworking.entity.Espaco;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class EspacoDTO extends RepresentationModel<EspacoDTO> {
    private Long id;
    private String nome;
    private String descricao;
    @Enumerated(EnumType.STRING)
    private EspacoType tipo;

    public EspacoDTO (Espaco entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.descricao = entity.getDescricao();
        this.tipo = entity.getTipo();
    }
}
