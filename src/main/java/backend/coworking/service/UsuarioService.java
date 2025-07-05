package backend.coworking.service;

import backend.coworking.dto.UsuarioDTO;
import backend.coworking.dto.UsuarioInsertDTO;
import backend.coworking.dto.RoleDTO;
import backend.coworking.entity.Usuario;
import backend.coworking.entity.Role;
import backend.coworking.exception.DatabaseException;
import backend.coworking.exception.ResourceNotFound;
import backend.coworking.projection.UserDetailsProjection;
import backend.coworking.repository.UsuarioRepository;
import backend.coworking.repository.RoleRepository;
import backend.coworking.resource.UsuarioResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> findAll (Pageable pageable) {
        Page<Usuario> list = usuarioRepository.findAll(pageable);
        return list.map(
                u -> new UsuarioDTO(u)
                        .add(linkTo(methodOn(UsuarioResource.class).findAll(null)).withSelfRel())
                        .add(linkTo(methodOn(UsuarioResource.class).findById(u.getId())).withRel("Informações do usuário"))
        );
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findById (Long id) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        Usuario usuario = opt.orElseThrow(() -> new ResourceNotFound("Usuário não encontrado | ID: " + id));
        return new UsuarioDTO(usuario)
                .add(linkTo(methodOn(UsuarioResource.class).findById(usuario.getId())).withSelfRel())
                .add(linkTo(methodOn(UsuarioResource.class).findAll(null)).withRel("Todos os usuários"))
                .add(linkTo(methodOn(UsuarioResource.class).update(usuario.getId(), null)).withRel("Atualizar usuário"))
                .add(linkTo(methodOn(UsuarioResource.class).delete(usuario.getId())).withRel("Deletar usuário"));
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findByEmail (String email) {
        Optional<Usuario> opt = usuarioRepository.findByEmail(email);
        Usuario usuario = opt.orElseThrow(() -> new ResourceNotFound("Usuário não encontrado | Email: " + email));
        return new UsuarioDTO(usuario)
                .add(linkTo(methodOn(UsuarioResource.class).findById(usuario.getId())).withSelfRel())
                .add(linkTo(methodOn(UsuarioResource.class).findAll(null)).withRel("Todos os usuários"))
                .add(linkTo(methodOn(UsuarioResource.class).update(usuario.getId(), null)).withRel("Atualizar usuário"))
                .add(linkTo(methodOn(UsuarioResource.class).delete(usuario.getId())).withRel("Deletar usuário"));
    }

    @Transactional
    public UsuarioDTO insert (UsuarioInsertDTO dto) {
        Usuario entity = new Usuario();
        this.copiarDTOParaEntidade(dto, entity);
        entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        Usuario novo = usuarioRepository.save(entity);
        return new UsuarioDTO(novo)
                .add(linkTo(methodOn(UsuarioResource.class).findById(novo.getId())).withRel("Encontrar usuário por ID"))
                .add(linkTo(methodOn(UsuarioResource.class).findAll(null)).withRel("Todos os usuários"))
                .add(linkTo(methodOn(UsuarioResource.class).update(novo.getId(), null)).withRel("Atualizar usuário"))
                .add(linkTo(methodOn(UsuarioResource.class).delete(novo.getId())).withRel("Deletar usuário"));
    }

    @Transactional
    public UsuarioDTO update (Long id, UsuarioInsertDTO dto) {
        try {
            Usuario entity = usuarioRepository.getReferenceById(id);
            this.copiarDTOParaEntidade(dto, entity);
            entity.setSenha(passwordEncoder.encode(dto.getSenha()));
            entity = usuarioRepository.save(entity);
            return new UsuarioDTO(entity)
                    .add(linkTo(methodOn(UsuarioResource.class).findById(dto.getId())).withRel("Encontrar usuário por ID"))
                    .add(linkTo(methodOn(UsuarioResource.class).findAll(null)).withRel("Todos os usuários"))
                    .add(linkTo(methodOn(UsuarioResource.class).delete(dto.getId())).withRel("Deletar usuário"));
        }
        catch (EntityNotFoundException ex) {
            throw new ResourceNotFound("Usuário não encontrado: ID " + id);
        }
    }

    @Transactional
    public void delete (Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFound("Usuário não encontrado: ID" + id);
        }

        try {
            usuarioRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("Violação de integridade: " + ex.getMessage());
        }
    }
    
    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = usuarioRepository.findUserAndRoleByEmail(username);

        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(result.get(0).getUsername());
        usuario.setSenha(result.get(0).getPassword());
        for (UserDetailsProjection p : result) {
            usuario.addRole(new Role(p.getRoleId(), p.getAuthority()));
        }

        return usuario;
    }
    
    private void copiarDTOParaEntidade (UsuarioDTO dto, Usuario entity) {
        entity.setNome(dto.getNome());
        entity.setCargo(dto.getCargo());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        
        entity.getRoles().clear();
        for (RoleDTO role : dto.getRoles()) {
            Role r = roleRepository.getReferenceById(role.getId());
            entity.getRoles().add(r);
        }
    }
}
