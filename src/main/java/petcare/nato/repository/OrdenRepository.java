package petcare.nato.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import petcare.nato.model.Orden;
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByClienteId(Long clienteId);
}