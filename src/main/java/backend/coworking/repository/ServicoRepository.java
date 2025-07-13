package backend.coworking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.coworking.entity.Servico;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
}
