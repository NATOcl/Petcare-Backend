package petcare.nato.service;

import petcare.nato.model.Mascota;
import petcare.nato.repository.MascotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MascotaService {

    private final MascotaRepository repo;

    public MascotaService(MascotaRepository repo) {
        this.repo = repo;
    }

    public List<Mascota> obtenerTodas() {
        return repo.findAll();
    }

    public Optional<Mascota> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public Mascota guardar(Mascota mascota) {
        return repo.save(mascota);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
