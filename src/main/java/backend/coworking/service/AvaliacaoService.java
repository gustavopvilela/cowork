package backend.coworking.service;

import backend.coworking.dto.AvaliacaoDTO;
import backend.coworking.dto.EspacoDTO;
import backend.coworking.dto.EspacoStatsDTO;
import backend.coworking.dto.insert.AvaliacaoInsertDTO;
import backend.coworking.entity.Avaliacao;
import backend.coworking.entity.Espaco;
import backend.coworking.entity.Reserva;
import backend.coworking.entity.Usuario;
import backend.coworking.exception.DatabaseException;
import backend.coworking.exception.ResourceNotFound;
import backend.coworking.repository.AvaliacaoRepository;
import backend.coworking.repository.EspacoRepository;
import backend.coworking.repository.ReservaRepository;
import backend.coworking.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Service
public class AvaliacaoService {
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

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

    @Transactional
    public AvaliacaoDTO update (Long id, AvaliacaoInsertDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario autor = usuarioRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFound("Usuário não encontrado."));

        Avaliacao avaliacao = avaliacaoRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Avaliação não encontrada: ID " + id));

        if (!avaliacao.getAutor().getId().equals(autor.getId())) {
            throw new DatabaseException("Acesso negado: você não é o autor desta avaliação.");
        }

        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setDataAvaliacao(Instant.now());
        avaliacao = avaliacaoRepository.save(avaliacao);
        return new AvaliacaoDTO(avaliacao);
    }

    @Transactional
    public void delete (Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioLogado = usuarioRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));

        Avaliacao avaliacao = avaliacaoRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Avaliação não encontrada: ID " + id));

        /* Verificando se o usuário logado é o autor ou o admin */
        boolean isAutor = avaliacao.getAutor().getId().equals(usuarioLogado.getId());
        boolean isAdmin = usuarioLogado.hasRole("ROLE_ADMIN");

        if (!isAutor && !isAdmin) {
            throw new DatabaseException("Acesso negado: você não tem permissão para deletar essa avaliação.");
        }

        try {
            avaliacaoRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade: " + e.getMessage());
        }
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

    @Transactional(readOnly = true)
    public EspacoStatsDTO getEspacoStats (Long id) {
        Espaco espaco = espacoRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Espaço não encontrado: ID" + id));

        List<Avaliacao> avaliacoes = avaliacaoRepository.findAllByEspacoId(id);

        Long contagemNotas1 = avaliacoes.stream().filter(a -> a.getNota() == 1).count();
        Long contagemNotas2 = avaliacoes.stream().filter(a -> a.getNota() == 2).count();
        Long contagemNotas3 = avaliacoes.stream().filter(a -> a.getNota() == 3).count();
        Long contagemNotas4 = avaliacoes.stream().filter(a -> a.getNota() == 4).count();
        Long contagemNotas5 = avaliacoes.stream().filter(a -> a.getNota() == 5).count();
        BigDecimal media = BigDecimal.valueOf(avaliacoes.stream().mapToInt(Avaliacao::getNota).average().orElse(0.0)).setScale(2, RoundingMode.HALF_UP);

        return new EspacoStatsDTO(
                new EspacoDTO(espaco),
                contagemNotas1,
                contagemNotas2,
                contagemNotas3,
                contagemNotas4,
                contagemNotas5,
                media
        );
    }
}
