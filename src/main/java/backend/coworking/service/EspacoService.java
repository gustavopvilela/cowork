package backend.coworking.service;

import backend.coworking.dto.EspacoDTO;
import backend.coworking.entity.Espaco;
import backend.coworking.exception.DatabaseException;
import backend.coworking.exception.ResourceNotFound;
import backend.coworking.repository.EspacoRepository;
import backend.coworking.resource.EspacoResource;
import backend.coworking.resource.UsuarioResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EspacoService {
    @Autowired
    private EspacoRepository espacoRepository;

    @Transactional(readOnly = true)
    public Page<EspacoDTO> findAll(Pageable pageable) {
        Page<Espaco> list = espacoRepository.findAll(pageable);
        return list.map(
                u -> new EspacoDTO(u)
                        .add(linkTo(methodOn(EspacoResource.class).findAll(null)).withSelfRel())
                        .add(linkTo(methodOn(EspacoResource.class).findById(u.getId())).withRel("Informações do espaço"))
        );
    }

    @Transactional(readOnly = true)
    public EspacoDTO findById(Long id) {
        Espaco espaço = espacoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Espaço não encontrado"));
        return new EspacoDTO(espaço)
                .add(linkTo(methodOn(EspacoResource.class).findById(espaço.getId())).withSelfRel())
                .add(linkTo(methodOn(EspacoResource.class).findAll(null)).withRel("Todos os espaços"))
                .add(linkTo(methodOn(EspacoResource.class).update(espaço.getId(), null)).withRel("Atualizar espaço"))
                .add(linkTo(methodOn(EspacoResource.class).delete(espaço.getId())).withRel("Deletar espaço"));
    }
    
    @Transactional
    public EspacoDTO insert (EspacoDTO dto) {
        Espaco espaco = new Espaco();
        this.copiarDtoParaEntidade(dto, espaco);
        Espaco novo = espacoRepository.save(espaco);
        return new EspacoDTO(novo)
                .add(linkTo(methodOn(EspacoResource.class).findById(novo.getId())).withRel("Encontrar espaço por ID"))
                .add(linkTo(methodOn(EspacoResource.class).findAll(null)).withRel("Todos os espaços"))
                .add(linkTo(methodOn(EspacoResource.class).update(novo.getId(), null)).withRel("Atualizar espaço"))
                .add(linkTo(methodOn(EspacoResource.class).delete(novo.getId())).withRel("Deletar espaço"));
    }

    @Transactional
    public EspacoDTO update(Long id, EspacoDTO dto) {
        try {
            Espaco espaco = espacoRepository.getReferenceById(id);
            this.copiarDtoParaEntidade(dto, espaco);
            espaco = espacoRepository.save(espaco);

            return new EspacoDTO(espaco)
                    .add(linkTo(methodOn(EspacoResource.class).findById(espaco.getId())).withRel("Encontrar espaço por ID"))
                    .add(linkTo(methodOn(EspacoResource.class).findAll(null)).withRel("Todos os espaços"))
                    .add(linkTo(methodOn(EspacoResource.class).delete(espaco.getId())).withRel("Deletar espaço"));
        }
        catch (EntityNotFoundException ex) {
            throw new ResourceNotFound("Espaço não encontrado: ID " + id);
        }
    }
    
    @Transactional
    public void delete(Long id) {
        if (!espacoRepository.existsById(id)) {
            throw new ResourceNotFound("Espaço não encontrado: ID" + id);
        }
        try {
            espacoRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("Violação de integridade: " + ex.getMessage());
        }
    }
    
    private void copiarDtoParaEntidade (EspacoDTO dto, Espaco entity) {
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setTipo(dto.getTipo());
    }
}
