package backend.coworking.service;

import backend.coworking.dto.ServicoDTO;
import backend.coworking.entity.Servico;
import backend.coworking.exception.DatabaseException;
import backend.coworking.exception.ResourceNotFound;
import backend.coworking.repository.ServicoRepository;
import backend.coworking.resource.ServicoResource;
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
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Transactional(readOnly = true)
    public Page<ServicoDTO> findAll(Pageable pageable) {
        Page<Servico> page = servicoRepository.findAll(pageable);
        return page.map(
                s -> new ServicoDTO(s)
                        .add(linkTo(methodOn(ServicoResource.class).findAll(null)).withSelfRel())
                        .add(linkTo(methodOn(ServicoResource.class).findById(s.getId())).withRel("Detalhes do serviço"))
        );
    }

    @Transactional(readOnly = true)
    public ServicoDTO findById(Long id) {
        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Serviço não encontrado: ID " + id));
        return new ServicoDTO(entity)
                .add(linkTo(methodOn(ServicoResource.class).findById(id)).withSelfRel())
                .add(linkTo(methodOn(ServicoResource.class).findAll(null)).withRel("Todos os serviços"))
                .add(linkTo(methodOn(ServicoResource.class).update(id, null)).withRel("Atualizar serviço"))
                .add(linkTo(methodOn(ServicoResource.class).delete(id)).withRel("Deletar serviço"));
    }

    @Transactional
    public ServicoDTO insert(ServicoDTO dto) {
        Servico entity = new Servico();
        copyDtoToEntity(dto, entity);
        Servico novo = servicoRepository.save(entity);
        return new ServicoDTO(novo)
                .add(linkTo(methodOn(ServicoResource.class).findById(novo.getId())).withRel("Encontrar serviço por ID"))
                .add(linkTo(methodOn(ServicoResource.class).findAll(null)).withRel("Todos os serviços"))
                .add(linkTo(methodOn(ServicoResource.class).update(novo.getId(), null)).withRel("Atualizar serviço"))
                .add(linkTo(methodOn(ServicoResource.class).delete(novo.getId())).withRel("Deletar serviço"));
    }

    @Transactional
    public ServicoDTO update(Long id, ServicoDTO dto) {
        try {
            Servico entity = servicoRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = servicoRepository.save(entity);
            return new ServicoDTO(entity)
                    .add(linkTo(methodOn(ServicoResource.class).findById(id)).withRel("Encontrar serviço por ID"))
                    .add(linkTo(methodOn(ServicoResource.class).findAll(null)).withRel("Todos os serviços"))
                    .add(linkTo(methodOn(ServicoResource.class).delete(id)).withRel("Deletar serviço"));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFound("Serviço não encontrado: ID " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new ResourceNotFound("Serviço não encontrado: ID " + id);
        }
        try {
            servicoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade: " + e.getMessage());
        }
    }

    private void copyDtoToEntity(ServicoDTO dto, Servico entity) {
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
    }
}