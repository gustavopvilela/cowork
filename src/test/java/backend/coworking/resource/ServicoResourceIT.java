package backend.coworking.resource;

import backend.coworking.dto.ServicoDTO;
import backend.coworking.exception.DatabaseException;
import backend.coworking.exception.ResourceNotFound;
import backend.coworking.service.ServicoService;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ServicoResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ServicoService servicoService;

    private Long idExistente;
    private Long idInexistente;
    private Long idDependente;
    private ServicoDTO servicoDTO;

    @BeforeEach
    void setUp() throws Exception {
        idExistente = 1L;
        idInexistente = 99L;
        idDependente = 2L;

        servicoDTO = new ServicoDTO(idExistente, "Café Premium", "Acesso ilimitado a café gourmet.");
    }

    @Test
    @DisplayName("Deve retornar 200 OK e uma página de serviços")
    public void findAllShouldReturnPageOfServicos() throws Exception {
        Page<ServicoDTO> paginaDeServicos = new PageImpl<>(List.of(servicoDTO));
        when(servicoService.findAll(any(Pageable.class))).thenReturn(paginaDeServicos);

        ResultActions result = mockMvc.perform(get("/servico")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].nome").value("Café Premium"));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e um serviço quando ID existe")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void findByIdShouldReturnServicoWhenIdExists() throws Exception {
        when(servicoService.findById(idExistente)).thenReturn(servicoDTO);

        ResultActions result = mockMvc.perform(get("/servico/{id}", idExistente)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(idExistente));
        result.andExpect(jsonPath("$.nome").value("Café Premium"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do serviço não existe")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        when(servicoService.findById(idInexistente)).thenThrow(new ResourceNotFound("Serviço não encontrado"));

        ResultActions result = mockMvc.perform(get("/servico/{id}", idInexistente)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 201 Created ao inserir serviço como ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void insertShouldReturnCreatedWhenUserIsAdmin() throws Exception {
        ServicoDTO dtoParaInserir = new ServicoDTO(null, "Novo Serviço", "Descrição do novo serviço");
        when(servicoService.insert(any(ServicoDTO.class))).thenReturn(servicoDTO);

        ResultActions result = mockMvc.perform(post("/servico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoParaInserir)));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.nome").value("Café Premium"));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden ao tentar inserir serviço como PROFISSIONAL")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void insertShouldReturnForbiddenWhenUserIsNotAdmin() throws Exception {
        ResultActions result = mockMvc.perform(post("/servico")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(servicoDTO)));

        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao atualizar serviço como ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void updateShouldReturnOkWhenUserIsAdmin() throws Exception {
        when(servicoService.update(eq(idExistente), any(ServicoDTO.class))).thenReturn(servicoDTO);

        ResultActions result = mockMvc.perform(put("/servico/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(servicoDTO)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.nome").value("Café Premium"));
    }

    @Test
    @DisplayName("Deve retornar 204 No Content ao deletar serviço como ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void deleteShouldReturnNoContent() throws Exception {
        doNothing().when(servicoService).delete(idExistente);

        ResultActions result = mockMvc.perform(delete("/servico/{id}", idExistente));

        result.andExpect(status().isNoContent());
        verify(servicoService, times(1)).delete(idExistente);
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar deletar serviço com dependências")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void deleteShouldReturnBadRequestWhenViolacaoDeIntegridade() throws Exception {
        doThrow(new DatabaseException("Violação de integridade")).when(servicoService).delete(idDependente);

        ResultActions result = mockMvc.perform(delete("/servico/{id}", idDependente));

        result.andExpect(status().isBadRequest());
    }
}
