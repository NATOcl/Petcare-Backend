package petcare.nato.repository;

import petcare.nato.model.Mascota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByClienteId(Long clienteId);
}