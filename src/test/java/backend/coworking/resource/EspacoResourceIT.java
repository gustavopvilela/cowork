package backend.coworking.resource;

import backend.coworking.constant.EspacoType;
import backend.coworking.dto.EspacoDTO;
import backend.coworking.exception.ResourceNotFound;
import backend.coworking.service.EspacoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.time.Instant;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class EspacoResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EspacoService espacoService;

    private Long idExistente;
    private Long idInexistente;
    private EspacoDTO espacoDTO;

    @BeforeEach
    void setUp() throws Exception {
        idExistente = 1L;
        idInexistente = 99L;

        espacoDTO = new EspacoDTO();
        espacoDTO.setId(idExistente);
        espacoDTO.setNome("Sala reuniao 1");
        espacoDTO.setDescricao("Sala com projetor e quadro branco");
        espacoDTO.setCapacidade(10);
        espacoDTO.setTipo(EspacoType.SALA_REUNIAO);
    }

    @Test
    @DisplayName("Deve retornar 200 OK e uma página de espaços")
    public void findAllShouldReturnPageOfEspacos() throws Exception {
        Page<EspacoDTO> pageDeEspacos = new PageImpl<>(List.of(espacoDTO));
        when(espacoService.findAll(any(Pageable.class))).thenReturn(pageDeEspacos);

        ResultActions result = mockMvc.perform(get("/espaco")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].nome").value("Sala reuniao 1"));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e um espaço quando ID existe e usuário é PROFISSIONAL")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void findByIdShouldReturnEspacoWhenIdExists() throws Exception {
        when(espacoService.findById(idExistente)).thenReturn(espacoDTO);

        ResultActions result = mockMvc.perform(get("/espaco/{id}", idExistente));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(idExistente));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando ID do espaço não existe")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        when(espacoService.findById(idInexistente)).thenThrow(new ResourceNotFound("Espaço não encontrado"));

        ResultActions result = mockMvc.perform(get("/espaco/{id}", idInexistente));

        result.andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Deve retornar 201 Created ao inserir espaço com sucesso como ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void insertShouldReturnCreatedWhenAdmin() throws Exception {
        when(espacoService.insert(any(EspacoDTO.class))).thenReturn(espacoDTO);

        ResultActions result = mockMvc.perform(post("/espaco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(espacoDTO)));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.id").value(idExistente));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden ao tentar inserir espaço como PROFISSIONAL")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void insertShouldReturnForbiddenWhenIsNotAdmin() throws Exception {

        ResultActions result = mockMvc.perform(post("/espaco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(espacoDTO)));

        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao atualizar espaço com sucesso como ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void updateShouldReturnOkWhenAdmin() throws Exception {
        when(espacoService.update(eq(idExistente), any(EspacoDTO.class))).thenReturn(espacoDTO);

        ResultActions result = mockMvc.perform(put("/espaco/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(espacoDTO)));

        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 204 No Content ao deletar espaço com sucesso como ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void deleteShouldReturnNoContentWhenAdmin() throws Exception {
        doNothing().when(espacoService).delete(idExistente);

        ResultActions result = mockMvc.perform(delete("/espaco/{id}", idExistente));

        result.andExpect(status().isNoContent());
        verify(espacoService, times(1)).delete(idExistente);
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a lista de espaços disponíveis")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void findDisponibilidadeShoulReturnListOfEspacos() throws Exception {
        Instant inicio = Instant.parse("2025-08-01T10:00:00Z");
        Instant fim = Instant.parse("2025-08-01T12:00:00Z");

        when(espacoService.findEspacosDisponiveis(any(), any(), any(), any())).thenReturn(List.of(espacoDTO));

        ResultActions result = mockMvc.perform(get("/espaco/disponibilidade")
                .param("inicio", inicio.toString())
                .param("fim", fim.toString()));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").isArray());
        result.andExpect(jsonPath("$[0].nome").value("Sala reuniao 1"));
    }
    

}
