package backend.coworking.dto;

import backend.coworking.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RoleDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String authority;

    public RoleDTO (Role role) {
        this.id = role.getId();
        this.authority = role.getAuthority();
    }
}
