package backend.coworking.service;

import backend.coworking.dto.ReservaDTO;
import backend.coworking.dto.ReservaInsertDTO;
import backend.coworking.entity.Espaco;
import backend.coworking.entity.Reserva;
import backend.coworking.entity.Servico;
import backend.coworking.entity.Usuario;
import backend.coworking.exception.DatabaseException;
import backend.coworking.exception.ResourceNotFound;
import backend.coworking.repository.EspacoRepository;
import backend.coworking.repository.ReservaRepository;
import backend.coworking.repository.ServicoRepository;
import backend.coworking.repository.UsuarioRepository;
import backend.coworking.resource.ReservaResource;
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
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Transactional(readOnly = true)
    public Page<ReservaDTO> findAll(Pageable pageable) {
        Page<Reserva> page = reservaRepository.findAll(pageable);
        return page.map(
                r -> new ReservaDTO(r)
                        .add(linkTo(methodOn(ReservaResource.class).findAll(null)).withSelfRel())
                        .add(linkTo(methodOn(ReservaResource.class).findById(r.getId())).withRel("Detalhes da reserva"))
        );
    }

    @Transactional(readOnly = true)
    public ReservaDTO findById(Long id) {
        Reserva entity = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Reserva não encontrada: ID " + id));
        return new ReservaDTO(entity)
                .add(linkTo(methodOn(ReservaResource.class).findById(id)).withSelfRel())
                .add(linkTo(methodOn(ReservaResource.class).findAll(null)).withRel("Todas as reservas"))
                .add(linkTo(methodOn(ReservaResource.class).update(id, null)).withRel("Atualizar reserva"))
                .add(linkTo(methodOn(ReservaResource.class).delete(id)).withRel("Deletar reserva"));
    }

    @Transactional
    public ReservaDTO insert(ReservaInsertDTO dto) {
        Reserva entity = new Reserva();
        copyDtoToEntity(dto, entity);
        Reserva nova = reservaRepository.save(entity);
        return new ReservaDTO(nova)
                .add(linkTo(methodOn(ReservaResource.class).findById(nova.getId())).withRel("Encontrar reserva por ID"))
                .add(linkTo(methodOn(ReservaResource.class).findAll(null)).withRel("Todas as reservas"))
                .add(linkTo(methodOn(ReservaResource.class).update(nova.getId(), null)).withRel("Atualizar reserva"))
                .add(linkTo(methodOn(ReservaResource.class).delete(nova.getId())).withRel("Deletar reserva"));
    }

    @Transactional
    public ReservaDTO update(Long id, ReservaInsertDTO dto) {
        try {
            Reserva entity = reservaRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = reservaRepository.save(entity);
            return new ReservaDTO(entity)
                    .add(linkTo(methodOn(ReservaResource.class).findById(id)).withRel("Encontrar reserva por ID"))
                    .add(linkTo(methodOn(ReservaResource.class).findAll(null)).withRel("Todas as reservas"))
                    .add(linkTo(methodOn(ReservaResource.class).delete(id)).withRel("Deletar reserva"));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFound("Reserva não encontrada: ID " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new ResourceNotFound("Reserva não encontrada: ID " + id);
        }
        try {
            reservaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade: " + e.getMessage());
        }
    }

    private void copyDtoToEntity(ReservaInsertDTO dto, Reserva entity) {
        entity.setEntrada(dto.getEntrada());
        entity.setSaida(dto.getSaida());

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFound("Usuário não encontrado: ID " + dto.getUsuarioId()));
        entity.setUsuario(usuario);

        Espaco espaco = espacoRepository.findById(dto.getEspacoId())
                .orElseThrow(() -> new ResourceNotFound("Espaço não encontrado: ID " + dto.getEspacoId()));
        entity.setEspaco(espaco);

        if (dto.getServicoId() != null) {
            Servico servico = servicoRepository.findById(dto.getServicoId())
                    .orElseThrow(() -> new ResourceNotFound("Serviço não encontrado: ID " + dto.getServicoId()));
            entity.setServico(servico);
        } else {
            entity.setServico(null);
        }
    }
}