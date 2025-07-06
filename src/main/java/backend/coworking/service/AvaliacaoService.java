package backend.coworking.service;

import backend.coworking.dto.AvaliacaoDTO;
import backend.coworking.dto.insert.AvaliacaoInsertDTO;
import backend.coworking.entity.Avaliacao;
import backend.coworking.entity.Reserva;
import backend.coworking.entity.Usuario;
import backend.coworking.exception.DatabaseException;
import backend.coworking.exception.ResourceNotFound;
import backend.coworking.repository.AvaliacaoRepository;
import backend.coworking.repository.ReservaRepository;
import backend.coworking.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AvaliacaoService {
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public AvaliacaoDTO insert (Long reservaId, AvaliacaoInsertDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario autor = usuarioRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));
        Reserva reserva = reservaRepository.findById(reservaId).orElseThrow(() -> new ResourceNotFound("Reserva inexistente. ID: " + reservaId));

        /* A reserva deve pertencer ao usuário logado */
        if (!reserva.getUsuario().getId().equals(autor.getId())) {
            throw new DatabaseException("Acesso negado: essa reserva não pertence a você.");
        }

        /* A reserva já deve ter terminado */
        if (reserva.getSaida().isAfter(Instant.now())) {
            throw new DatabaseException("Você só pode avaliar reservas concluídas.");
        }

        /* A reserva só pode ser avaliada uma vez */
        if (avaliacaoRepository.existsByReservaId(reservaId)) {
            throw new DatabaseException("Esta reserva já foi avaliada.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setDataAvaliacao(Instant.now());
        avaliacao.setAutor(autor);
        avaliacao.setReserva(reserva);
        avaliacao = avaliacaoRepository.save(avaliacao);
        return new AvaliacaoDTO(avaliacao);
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoDTO> findByEspaco (Long espacoId, Pageable pageable) {
        Page<Avaliacao> page = avaliacaoRepository.findByEspacoId(espacoId, pageable);
        return page.map(AvaliacaoDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoDTO> findMinhasAvaliacoes (Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario autor = usuarioRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));

        Page<Avaliacao> page = avaliacaoRepository.findByAutorId(autor.getId(), pageable);
        return page.map(AvaliacaoDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AvaliacaoDTO> findByUsuario (Long usuarioId, Pageable pageable) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFound("Usuário não encontrado: ID " + usuarioId);
        }
        Page<Avaliacao> page = avaliacaoRepository.findByAutorId(usuarioId, pageable);
        return page.map(AvaliacaoDTO::new);
    }
}
