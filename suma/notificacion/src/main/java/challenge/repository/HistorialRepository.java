package challenge.repository;

import challenge.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialRepository extends JpaRepository<Historial, Long> {
}
