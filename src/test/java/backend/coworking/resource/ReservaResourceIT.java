package backend.coworking.resource;

import backend.coworking.dto.CancelamentoReservaDTO;
import backend.coworking.dto.EspacoDTO;
import backend.coworking.dto.ReservaDTO;
import backend.coworking.dto.UsuarioDTO;
import backend.coworking.dto.insert.ReservaInsertDTO;
import backend.coworking.exception.ResourceNotFound;
import backend.coworking.service.ReservaService;
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
import org.springframework.security.access.AccessDeniedException;


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
public class ReservaResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservaService reservaService;

    private Long idExistente;
    private Long idInexistente;
    private ReservaDTO reservaDTO;
    private ReservaInsertDTO reservaInsertDTO;

    @BeforeEach
    void setUp() throws Exception {

        idExistente = 1L;
        idInexistente = 99L;

        reservaInsertDTO = new ReservaInsertDTO(Instant.now(), Instant.now().plusSeconds(3600), 1L, 1l, 1l );

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setId(1L);
        EspacoDTO espaco = new EspacoDTO();
        espaco.setId(1L);
        reservaDTO = new ReservaDTO(idExistente, reservaInsertDTO.getEntrada(), reservaInsertDTO.getSaida(), usuario, espaco, null);
    }

    @Test
    @DisplayName("Deve retornar 200 OK e uma página de reservas para o ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void findAllShouldReturnPageForAdmin() throws Exception {
        Page<ReservaDTO> pagina = new PageImpl<>(List.of(reservaDTO));

        when(reservaService.findAll(any(Pageable.class))).thenReturn(pagina);

        ResultActions result = mockMvc.perform(get("/reserva"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].id").value(idExistente));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden para findAll quando usuário não é ADMIN")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void findAllShouldReturnForbiddenWhenUserIsNotAdmin() throws Exception {

        ResultActions result = mockMvc.perform(get("/reserva"));

        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a reserva quando ID existe")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void findByIdShoulReturnReservaWhenIdExists() throws Exception {
        when(reservaService.findById(idExistente)).thenReturn(reservaDTO);

        ResultActions result = mockMvc.perform(get("/reserva/{id}", idExistente));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(idExistente));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found para findById quando ID não existe")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        when(reservaService.findById(idInexistente)).thenThrow(new ResourceNotFound("Reserva não encontrada"));

        ResultActions result = mockMvc.perform(get("/reserva/{id}", idInexistente));

        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 201 Created ao criar uma reserva com sucesso")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void insertShouldReturnCreated() throws Exception {
        when(reservaService.insert(any(ReservaInsertDTO.class))).thenReturn(reservaDTO);

        ResultActions result = mockMvc.perform(post("/reserva")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaInsertDTO)));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao atualizar uma reserva com sucesso")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void updateShouldReturnOk() throws Exception {
        when(reservaService.update(eq(idExistente), any(ReservaInsertDTO.class))).thenReturn(reservaDTO);

        ResultActions result = mockMvc.perform(put("/reserva/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaInsertDTO)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(idExistente));
    }

    @Test
    @DisplayName("Deve retornar 204 No Content ao deletar uma reserva com sucesso")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void deleteShouldReturnNoContent() throws Exception {
        doNothing().when(reservaService).delete(idExistente);

        ResultActions result = mockMvc.perform(delete("/reserva/{id}", idExistente));

        result.andExpect(status().isNoContent());
        verify(reservaService, times(1)).delete(idExistente);
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden ao tentar deletar reserva de outro (sem ser admin)")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void deleteShouldReturnForbiddenWhenProfissionalIsNotAuthor() throws Exception {
        doThrow(new AccessDeniedException("Acesso negado")).when(reservaService).delete(anyLong());

        ResultActions result = mockMvc.perform(delete("/reserva/{id}", idExistente));

        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a página de reservas do usuário logado")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void encontrarMinhasReservasShouldReturnUserPage() throws Exception {

        Page<ReservaDTO> pagina = new PageImpl<>(List.of(reservaDTO));
        when(reservaService.findByUsuarioLogado(any(Pageable.class))).thenReturn(pagina);

        ResultActions result = mockMvc.perform(get("/reserva/me"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].id").value(idExistente));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e o total de reservas canceladas para o ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void cancelarPorPeriodoShouldReturnOkWhenUserIsAdmin() throws Exception {
        CancelamentoReservaDTO cancelamentoDTO = new CancelamentoReservaDTO(Instant.now(), Instant.now().plusSeconds(7200), "Manutenção");
        int totalCancelado = 5;
        when(reservaService.cancelarReservasPorPeriodo(any(CancelamentoReservaDTO.class))).thenReturn(totalCancelado);

        ResultActions result = mockMvc.perform(post("/reserva/cancelar-por-periodo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelamentoDTO)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.total_reservas_canceladas").value(totalCancelado));
        result.andExpect(jsonPath("$.motivo").value("Manutenção"));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden para cancelarPorPeriodo quando usuário não é ADMIN")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void cancelarPorPeriodoShouldReturnForbiddenwhenUserIsNotAdmin() throws Exception {
        CancelamentoReservaDTO cancelamentoDTO = new CancelamentoReservaDTO(Instant.now(), Instant.now().plusSeconds(7200), "Motivo qualquer");

        ResultActions result = mockMvc.perform(post("/reserva/cancelar-por-periodo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelamentoDTO)));

        result.andExpect(status().isForbidden());
    }


}
