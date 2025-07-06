package backend.coworking.dto;

import backend.coworking.entity.Servico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicoRankingDTO {
    private ServicoDTO servico;
    private Long quantidade;

    public ServicoRankingDTO(Servico servico, Long quantidade) {
        this.servico = new ServicoDTO(servico);
        this.quantidade = quantidade;
    }
}
