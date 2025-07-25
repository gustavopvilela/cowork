package backend.coworking.resource;

import backend.coworking.dto.ServicoRankingDTO;
import backend.coworking.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relatorio")
@Tag(name = "Relatórios", description = "Controller para relatórios de administração")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')") /* Desta forma, todos os endpoints só podem ser acessados por um admin */
public class RelatorioResource {
    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/ocupacao")
    @Operation(
            summary = "Retorna a taxa de ocupação dos espaços",
            description = "Dado um mês e um ano, este relatório retornará qual a taxa de ocupação total dos espaços dentro do intervalo do mês. O cálculo é feito de acordo com a porcentagem das horas totais reservadas com as horas totais possíveis no mês.",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Map<String, Double>> getTaxaOcupacao (
            @RequestParam int ano,
            @RequestParam int mes
    ) {
        Map<String, Double> taxa = relatorioService.getTaxaOcupacao(ano, mes);
        return ResponseEntity.ok().body(taxa);
    }

    @GetMapping("/servicos/top")
    @Operation(
            summary = "Retorna o ranking dos serviços mais usados",
            description = "Dadas todas as reservas, mostra os serviços mais requisitados por elas",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<ServicoRankingDTO>> getTopServicos () {
        List<ServicoRankingDTO> ranking = relatorioService.getTopServicosMaisUsados();
        return ResponseEntity.ok().body(ranking);
    }
}
