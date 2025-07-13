package backend.coworking.dto;

import backend.coworking.entity.Usuario;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
    @EqualsAndHashCode.Include
    private Long id;
    @NotBlank(message = "O campo de nome é obrigatório")
    private String nome;
    private String cargo;
    @NotBlank(message = "O campo de email é obrigatório")
    @Email(message = "Insira um e-mail válido")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "O campo de telefone é obrigatório")
    private String telefone;
    private Set<RoleDTO> roles = new HashSet<>();

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.cargo = usuario.getCargo();
        this.email = usuario.getEmail();
        this.telefone = usuario.getTelefone();

        usuario.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public boolean hasRole (String role) {
        return !roles
                .stream()
                .filter(r -> r.getAuthority().equals(role))
                .toList()
                .isEmpty();
    }
}
