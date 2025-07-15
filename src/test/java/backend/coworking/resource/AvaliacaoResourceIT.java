package backend.coworking.resource;

import backend.coworking.dto.AvaliacaoDTO;
import backend.coworking.dto.EspacoDTO;
import backend.coworking.dto.EspacoStatsDTO;
import backend.coworking.dto.UsuarioDTO;
import backend.coworking.dto.insert.AvaliacaoInsertDTO;
import backend.coworking.exception.ResourceNotFound;
import backend.coworking.service.AvaliacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AvaliacaoResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AvaliacaoService avaliacaoService;

    private Long idExistente;
    private Long idInexistente;
    private AvaliacaoInsertDTO avaliacaoInsertDTO;
    private AvaliacaoDTO avaliacaoDTO;

    @BeforeEach
    void setUp() throws Exception {
        idExistente = 1L;
        idInexistente = 99L;

        avaliacaoInsertDTO = new AvaliacaoInsertDTO();
        avaliacaoInsertDTO.setComentario("Comentario");
        avaliacaoInsertDTO.setNota(5);

        avaliacaoDTO = new AvaliacaoDTO(idExistente ,5,  "Comentario", Instant.now(), new UsuarioDTO());
    }

    @Test
    @DisplayName("Retorna 201 CREATED quando uma nova avaliação é criada com sucesso por um ADMIN.")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void insertShouldReturnCreatedForAdmin () throws Exception {
        when(avaliacaoService.insert(eq(idExistente), any(AvaliacaoInsertDTO.class))).thenReturn(avaliacaoDTO);

        ResultActions result= mockMvc.perform(post("/reserva/1/avaliar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoInsertDTO)));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(avaliacaoDTO.getId()));
        result.andExpect(jsonPath("$.comentario").value(avaliacaoDTO.getComentario()));
    }
    @Test
    @DisplayName("Retorna 201 CREATED quando uma nova avaliação é criada com sucesso por um PROFISSIONAL.")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void insertShouldReturnCreatedForProfissional() throws Exception {
        when(avaliacaoService.insert(eq(idExistente), any(AvaliacaoInsertDTO.class))).thenReturn(avaliacaoDTO);

        ResultActions result= mockMvc.perform(post("/reserva/1/avaliar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoInsertDTO)));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(avaliacaoDTO.getId()));
        result.andExpect(jsonPath("$.comentario").value(avaliacaoDTO.getComentario()));
    }

    @Test
    @DisplayName("Retorna 404 NOT FOUND quando tenta avaliar uma reserva que não existe.")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void insertShouldReturnNotFoundWhenReservaDoesNotExists() throws Exception {
        when(avaliacaoService.insert(eq(idInexistente), any(AvaliacaoInsertDTO.class)))
                .thenThrow(new ResourceNotFound("Reserva não encontrada"));

        ResultActions result = mockMvc.perform(post("/reserva/{id}/avaliar", idInexistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoInsertDTO)));

        result.andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Retorna 200 OK ao atualizar avaliação com sucesso (ADMIN)")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void updateShouldReturnOkWhenAdminUpdatesAvaliacao() throws Exception {
        AvaliacaoDTO dtoAtualizado = new AvaliacaoDTO(idExistente, 5, "Comentario atualizado", Instant.now(), null);

        when(avaliacaoService.update(eq(idExistente), any(AvaliacaoInsertDTO.class))).thenReturn(dtoAtualizado);

        ResultActions result = mockMvc.perform(put("/avaliacao/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoInsertDTO)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(idExistente));

        result.andExpect(jsonPath("$.comentario").value("Comentario atualizado"));
    }

    @Test
    @DisplayName("Retorna 200 OK ao atualizar avaliação com sucesso (ADMIN)")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void updateShouldReturnOkWhenProfissionalUpdatesAvaliacao() throws Exception {
        AvaliacaoDTO dtoAtualizado = new AvaliacaoDTO(idExistente, 5, "Comentario atualizado", Instant.now(), null);
        when(avaliacaoService.update(eq(idExistente), any(AvaliacaoInsertDTO.class))).thenReturn(dtoAtualizado);

        ResultActions result = mockMvc.perform(put("/avaliacao/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoInsertDTO)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.comentario").value("Comentario atualizado"));
    }

    @Test
    @DisplayName("Retorna 403 Forbidden quando tenta atualizar avaliação de outro usuário")
    @WithMockUser(username = "outro.user@gmail.com", authorities = "ROLE_PROFISSIONAL")
    public void updateShouldReturnForbiddenWhenUserIsNotAuthor() throws Exception {
        when(avaliacaoService.update(eq(idExistente), any(AvaliacaoInsertDTO.class))).thenThrow(new AccessDeniedException("Acesso negado"));

        ResultActions result = mockMvc.perform(put("/avaliacao/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(avaliacaoInsertDTO)));

        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Retorna 204 NO CONTENT quando a valiação é deletada com sucesso por um ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void deleteShouldReturnNoContentWhenAdminDeletesAvaliacao() throws Exception {

        doNothing().when(avaliacaoService).delete(idExistente);

        ResultActions result = mockMvc.perform(delete("/avaliacao/{id}", idExistente));

        result.andExpect(status().isNoContent());
        verify(avaliacaoService, times(1)).delete(idExistente);
    }

    @Test
    @DisplayName("Retorna 404 NOT FOUND quando tentar deletar uma avaliação que não existe.")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void deleteShouldReturnNotFoundWhenAvaliacaoDoesNotExist() throws Exception {

        doThrow(new ResourceNotFound("ID não encontrado")).when(avaliacaoService).delete(idInexistente);

        ResultActions result = mockMvc.perform(delete("/avaliacao/{id}", idInexistente));

        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Retona 200 OK quando o endpoint retorna uma pagina de avaliações de um espaço existente")
    public void findByEspacoShouldReturnPageOfAvaliacoes() throws Exception {

        AvaliacaoDTO av1 = new AvaliacaoDTO(1L, 5, "Otimo ambiente", null, null);
        AvaliacaoDTO av2 = new AvaliacaoDTO(2l, 3, "Internet poderia ser melhor", null, null);

        Page<AvaliacaoDTO> paginaAvaliacoes = new PageImpl<>(List.of(av1, av2));

        when(avaliacaoService.findByEspaco(eq(idExistente), any(Pageable.class))).thenReturn(paginaAvaliacoes);

        ResultActions result = mockMvc.perform(get("/espaco/{id}/avaliacoes", idExistente)
                .param("page", "0")
                .param("size", "10"));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.totalElements").value(2));
        result.andExpect(jsonPath("$.totalPages").value(1));

        result.andExpect(jsonPath("$.content").isArray());

        result.andExpect(jsonPath("$.content[0].id").value(1L));
        result.andExpect(jsonPath("$.content[0].comentario").value("Otimo ambiente"));

        result.andExpect(jsonPath("$.content[1].id").value(2L));
        result.andExpect(jsonPath("$.content[1].comentario").value("Internet poderia ser melhor"));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e as estatísticas de um espaço existente")
    public void getEspacoStatsShouldReturnStatsDTO() throws Exception {

        EspacoDTO espaco = new EspacoDTO();
        espaco.setId(idExistente);
        EspacoStatsDTO statsDTO = new EspacoStatsDTO(espaco, 1L, 2L, 3L, 4L, 5L, new BigDecimal("4.5"));

        when(avaliacaoService.getEspacoStats(idExistente)).thenReturn(statsDTO);

        ResultActions result = mockMvc.perform(get("/espaco/{id}/avaliacoes/status", idExistente));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.espaco.id").value(idExistente));
        result.andExpect(jsonPath("$.media").value(4.5));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a página de avaliações do usuário logado")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void findMinhasAvaliacoesShouldReturnPageForAuthenticatedUser() throws Exception {
        Page<AvaliacaoDTO> minhaPagina = new PageImpl<>(List.of(avaliacaoDTO));
        when(avaliacaoService.findMinhasAvaliacoes(any(Pageable.class))).thenReturn(minhaPagina);

        ResultActions result = mockMvc.perform(get("/avaliacao/me"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(1));
        result.andExpect(jsonPath("$.content[0].id").value(idExistente));
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized ao buscar 'minhas avaliações' sem estar logado")
    public void findMinhasAvaliacoesShouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        ResultActions result = mockMvc.perform(get("/avaliacao/me"));
        result.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a página de avaliações de um usuário para o ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void findByUserShouldReturnPageWhenUserIsAdmin() throws Exception {
        Long idOutroUsuario = 2L;
        Page<AvaliacaoDTO> paginaOutroUsuario = new PageImpl<>(List.of(avaliacaoDTO));
        when(avaliacaoService.findByUsuario(eq(idOutroUsuario), any(Pageable.class))).thenReturn(paginaOutroUsuario);

        ResultActions result = mockMvc.perform(get("/usuario/{id}/avaliacoes", idOutroUsuario));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(1));
        result.andExpect(jsonPath("$.content[0].id").value(idExistente));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden ao buscar avaliações de outro usuário como PROFISSIONAL")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void findByUserShouldReturnForbiddenWhenUserIsNotAdmin() throws Exception {
        Long idOutroUsuario = 2L;

        ResultActions result = mockMvc.perform(get("/usuario/{id}/avaliacoes", idOutroUsuario));

        result.andExpect(status().isForbidden());
    }

}
