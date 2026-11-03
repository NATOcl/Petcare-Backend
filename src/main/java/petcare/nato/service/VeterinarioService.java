package petcare.nato.service;

import petcare.nato.model.Veterinario;
import petcare.nato.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarioService {

    private final VeterinarioRepository repo;

    public VeterinarioService(VeterinarioRepository repo) {
        this.repo = repo;
    }

    public List<Veterinario> obtenerTodos() {
        return repo.findAll();
    }

    public Optional<Veterinario> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public Veterinario guardar(Veterinario veterinario) {
        return repo.save(veterinario);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}