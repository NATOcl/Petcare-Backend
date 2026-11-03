package petcare.nato.service;

import petcare.nato.model.Cita;
import petcare.nato.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepository repo;

    public CitaService(CitaRepository repo) {
        this.repo = repo;
    }

    public List<Cita> obtenerTodas() {
        return repo.findAll();
    }

    public Optional<Cita> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public Cita guardar(Cita cita) {
        return repo.save(cita);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}