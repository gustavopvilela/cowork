package backend.coworking.resource;

import backend.coworking.dto.ServicoDTO;
import backend.coworking.dto.ServicoRankingDTO;
import backend.coworking.service.RelatorioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class RelatorioResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RelatorioService relatorioService;

    @Test
    @DisplayName("Deve retornar 200 OK e a taxa de ocupação quando o usuário é ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void getTaxaOcupacaoShouldReturnOkWhenUserIsAdmin() throws Exception {
        int ano = 2024;
        int mes = 7;

        Map<String, Double> taxaOcupacaoMock = Map.of("taxaOcupacaoPercentual", 75.5);

        when(relatorioService.getTaxaOcupacao(ano, mes)).thenReturn(taxaOcupacaoMock);

        ResultActions result = mockMvc.perform(get("/relatorio/ocupacao")
                .param("ano", String.valueOf(ano))
                .param("mes", String.valueOf(mes)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.taxaOcupacaoPercentual").value(75.5));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden para taxa de ocupação quando o usuário NÃO é ADMIN")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void getTaxaOcupacaoShouldReturnForbiddenWhenUserIsProfessional() throws Exception {

        ResultActions result = mockMvc.perform(get("/relatorio/ocupacao")
                .param("ano", String.valueOf(2024))
                .param("mes", String.valueOf(7)));

        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 200 OK e o ranking de serviços quando o usuário é ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void getTopServicosShouldReturnOkWhenUserIsAdmin() throws Exception {

        ServicoDTO servicoCafe = new ServicoDTO(1L, "Café Premium", "Café gourmet");
        ServicoRankingDTO rankingCafe = new ServicoRankingDTO(servicoCafe, 50L);

        ServicoDTO servicoInternet = new ServicoDTO(2L, "Internet Rápida", "Fibra óptica");
        ServicoRankingDTO rankingInternet = new ServicoRankingDTO(servicoInternet, 45L);

        List<ServicoRankingDTO> rankingMock = List.of(rankingCafe, rankingInternet);

        when(relatorioService.getTopServicosMaisUsados()).thenReturn(rankingMock);

        ResultActions result = mockMvc.perform(get("/relatorio/servicos/top"));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.length()").value(2));
        result.andExpect(jsonPath("$[0].servico.nome").value("Café Premium"));
        result.andExpect(jsonPath("$[0].quantidade").value(50));
        result.andExpect(jsonPath("$[1].servico.nome").value("Internet Rápida"));
        result.andExpect(jsonPath("$[1].quantidade").value(45));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden para ranking de serviços quando o usuário NÃO é ADMIN")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void getTopServicosShouldReturnForbiddenWhenUserIsNotAdmin() throws Exception {

        ResultActions result = mockMvc.perform(get("/relatorio/servicos/top"));

        result.andExpect(status().isForbidden());
    }
}
