package backend.coworking.repository;

import backend.coworking.entity.Avaliacao;
import backend.coworking.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    boolean existsByReservaId (Long reservaId);

    @Query("SELECT a FROM Avaliacao a WHERE a.reserva.espaco.id = :espacoId")
    Page<Avaliacao> findByEspacoId (Long espacoId, Pageable pageable);

    @Query("SELECT a FROM Avaliacao a WHERE a.reserva.espaco.id = :espacoId")
    List<Avaliacao> findAllByEspacoId (Long espacoId);

    Page<Avaliacao> findByAutorId (Long autorId, Pageable pageable);
}
