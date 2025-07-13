package backend.coworking.dto.insert;

import backend.coworking.dto.UsuarioDTO;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioInsertDTO extends UsuarioDTO {
    @Size(min = 2, max = 64)
    private String senha;

    public UsuarioInsertDTO(UsuarioDTO usuario) {
        this.setId(usuario.getId());
        this.setNome(usuario.getNome());
        this.setCargo(usuario.getCargo());
        this.setEmail(usuario.getEmail());
        this.setTelefone(usuario.getTelefone());
        this.setRoles(usuario.getRoles());
    }
}
