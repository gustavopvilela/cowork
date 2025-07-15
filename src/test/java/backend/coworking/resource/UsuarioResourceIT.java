package backend.coworking.resource;

import backend.coworking.dto.RoleDTO;
import backend.coworking.dto.UsuarioDTO;
import backend.coworking.dto.insert.UsuarioInsertDTO;
import backend.coworking.entity.Usuario;
import backend.coworking.exception.ResourceNotFound;
import backend.coworking.repository.UsuarioRepository;
import backend.coworking.service.UsuarioService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.http.MediaType;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UsuarioResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private Long idExistente;
    private Long idInexistente;
    private UsuarioDTO usuarioDTO;
    private UsuarioInsertDTO usuarioInsertDTO;
    private String emailExistente;

    @BeforeEach
    void setUp() {
        idExistente = 1L;
        idInexistente = 99L;
        emailExistente = "carlos.sainz@email.com";

        usuarioDTO = new UsuarioDTO(idExistente, "Carlos Sainz", "Piloto", emailExistente, "123456789", Set.of(new RoleDTO(2L, "ROLE_PROFISSIONAL")));

        usuarioInsertDTO = new UsuarioInsertDTO();
        usuarioInsertDTO.setId(idExistente);
        usuarioInsertDTO.setNome("Carlos Sainz");
        usuarioInsertDTO.setCargo("Piloto");
        usuarioInsertDTO.setEmail(emailExistente);
        usuarioInsertDTO.setTelefone("123456789");
        usuarioInsertDTO.setRoles(Set.of(new RoleDTO(2L, "ROLE_PROFISSIONAL")));
        usuarioInsertDTO.setSenha("senha123");
    }

    @Test
    @DisplayName("Deve retornar 200 OK e uma página de usuários para o ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void findAllShouldReturnPageForAdmin() throws Exception {
        Page<UsuarioDTO> pagina = new PageImpl<>(List.of(usuarioDTO));
        when(usuarioService.findAll(any(Pageable.class))).thenReturn(pagina);

        ResultActions result = mockMvc.perform(get("/usuario"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].nome").value("Carlos Sainz"));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden para findAll quando usuário não é ADMIN")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void findAllShouldReturnForbiddenWhenUserIsNotAdmin() throws Exception {
        ResultActions result = mockMvc.perform(get("/usuario"));

        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 200 OK e um usuário quando ID existe para ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void findByIdShouldReturnUsuarioForAdmin() throws Exception {
        when(usuarioService.findById(idExistente)).thenReturn(usuarioDTO);

        ResultActions result = mockMvc.perform(get("/usuario/{id}", idExistente));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(idExistente));
        result.andExpect(jsonPath("$.nome").value("Carlos Sainz"));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found para findById quando ID não existe")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        when(usuarioService.findById(idInexistente)).thenThrow(new ResourceNotFound("Usuário não encontrado"));

        ResultActions result = mockMvc.perform(get("/usuario/{id}", idInexistente));

        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden para findById quando usuário não é ADMIN")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void findByIdShouldReturnForbiddenWhenUserIsNotAdmin() throws Exception {

        ResultActions result = mockMvc.perform(get("/usuario/{id}", idExistente));

        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 200 OK e um usuário ao buscar por email existente")
    public void findByEmailShouldReturnUser() throws Exception {
        when(usuarioService.findByEmail(emailExistente)).thenReturn(usuarioDTO);

        ResultActions result = mockMvc.perform(get("/usuario/email/{email}", emailExistente));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.email").value(emailExistente));
    }

    @Test
    @DisplayName("Deve retornar 201 Created ao inserir usuário como ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void insertShouldReturnCreatedWhenUserIsAdmin() throws Exception {
        when(usuarioService.insert(any(UsuarioInsertDTO.class))).thenReturn(usuarioDTO);

        ResultActions result = mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioInsertDTO)));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao atualizar usuário como ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void updateShouldReturnOkWhenUserIsAdmin() throws Exception {
        when(usuarioService.update(eq(idExistente), any(UsuarioInsertDTO.class))).thenReturn(usuarioDTO);

        ResultActions result = mockMvc.perform(put("/usuario/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioInsertDTO)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.nome").value("Carlos Sainz"));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden ao tentar atualizar usuário como PROFISSIONAL")
    @WithMockUser(authorities = "ROLE_PROFISSIONAL")
    public void updateShouldReturnForbiddenWhenUserIsNotAdmin() throws Exception {

        ResultActions result = mockMvc.perform(put("/usuario/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioInsertDTO)));

        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 204 No Content ao deletar usuário como ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void deleteShouldReturnNoContentWhenUserIsAdmin() throws Exception {
        doNothing().when(usuarioService).delete(idExistente);

        ResultActions result = mockMvc.perform(delete("/usuario/{id}", idExistente));

        result.andExpect(status().isNoContent());
        verify(usuarioService, times(1)).delete(idExistente);
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao tentar deletar usuário com ID inexistente")
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        doThrow(new ResourceNotFound("Usuário não encontrado")).when(usuarioService).delete(idInexistente);

        ResultActions result = mockMvc.perform(delete("/usuario/{id}", idInexistente));

        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 200 OK e os dados do usuário logado")
    @WithMockUser(username = "carlos.sainz@email.com", authorities = "ROLE_PROFISSIONAL")
    public void getMeShouldReturnDataOfLoggedUser() throws Exception {
        when(usuarioService.getMe()).thenReturn(usuarioDTO);

        ResultActions result = mockMvc.perform(get("/usuario/me"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.email").value("carlos.sainz@email.com"));
    }


}
