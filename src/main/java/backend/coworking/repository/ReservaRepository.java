package backend.coworking.repository;

import backend.coworking.dto.ServicoRankingDTO;
import backend.coworking.entity.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    /* Consulta para cancelamento de reservas em determinado período */
    @Modifying
    @Query("DELETE FROM Reserva r WHERE r.entrada >= :inicio AND r.saida <= :fim")
    int deleteByPeriodo(Instant inicio, Instant fim);

    /* Consulta para retornar as reservas de acordo com um usuário */
    Page<Reserva> findByUsuarioId (Long usuarioId, Pageable pageable);

    @Query("SELECT r FROM Reserva r WHERE r.entrada >= :inicioMes AND r.saida <= :fimMes")
    List<Reserva> findAllByPeriodo(Instant inicioMes, Instant fimMes);

    @Query("SELECT new backend.coworking.dto.ServicoRankingDTO(s, COUNT(r.id)) " +
            "FROM Reserva r JOIN r.servico s " +
            "GROUP BY s " +
            "ORDER BY COUNT(r.id) DESC")
    List<ServicoRankingDTO> findTopServicos();
}
