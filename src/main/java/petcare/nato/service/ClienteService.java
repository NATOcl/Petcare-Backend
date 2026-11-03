package petcare.nato.service;

import petcare.nato.model.Cliente;
import petcare.nato.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    public List<Cliente> obtenerTodos() {
        return repo.findAll();
    }

    public Optional<Cliente> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public Cliente guardar(Cliente cliente) {
        return repo.save(cliente);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}