package petcare.nato.service;

import petcare.nato.model.Servicio;
import petcare.nato.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    private final ServicioRepository repo;

    public ServicioService(ServicioRepository repo) {
        this.repo = repo;
    }

    public List<Servicio> obtenerTodos() {
        return repo.findAll();
    }

    public Optional<Servicio> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public Servicio guardar(Servicio servicio) {
        return repo.save(servicio);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}