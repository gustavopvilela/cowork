package backend.coworking.repository;

import backend.coworking.constant.EspacoType;
import backend.coworking.entity.Espaco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EspacoRepository extends JpaRepository<Espaco, Long> {
    @Query("""
        SELECT e
        FROM Espaco e
        WHERE (:tipo IS NULL OR e.tipo = :tipo) AND
              (:capacidade IS NULL OR e.capacidade >= :capacidade) AND
              e.id NOT IN (
                    SELECT r.espaco.id
                    FROM Reserva r
                    WHERE (r.entrada <= :fim AND r.saida >= :inicio)
              )
    """)
    List<Espaco> findEspacosDisponiveis (Instant inicio, Instant fim, EspacoType tipo, Integer capacidade);
}
