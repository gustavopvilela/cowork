package backend.coworking.repository;

import backend.coworking.entity.Usuario;
import backend.coworking.projection.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    @Query(nativeQuery = true, value = """
        SELECT u.email AS username,
               u.senha AS password,
               r.id as roleId,
               r.authority
        FROM usuario u
        INNER JOIN usuario_role ur ON u.id = ur.usuario_id
        INNER JOIN role r ON r.id = ur.role_id
        WHERE u.email = :username
    """)
    List<UserDetailsProjection> findUserAndRoleByEmail (String username);
}
