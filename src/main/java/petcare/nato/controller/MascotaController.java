package petcare.nato.controller;

import petcare.nato.model.Mascota;
import petcare.nato.repository.MascotaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaRepository repo;

    public MascotaController(MascotaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Mascota> listar() {
        return repo.findAll();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Mascota> listarPorCliente(@PathVariable Long clienteId) {
        return repo.findByClienteId(clienteId);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Mascota> crear(@Valid @RequestBody Mascota req) {
        Mascota m = repo.save(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(m);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Mascota> actualizar(@PathVariable Long id, @Valid @RequestBody Mascota req) {
        return repo.findById(id)
                .map(m -> {
                    m.setNombre(req.getNombre());
                    m.setEspecie(req.getEspecie());
                    m.setRaza(req.getRaza());
                    m.setFechaNacimiento(req.getFechaNacimiento());
                    m.setPeso(req.getPeso());
                    m.setNumeroMicrochip(req.getNumeroMicrochip());
                    m.setCliente(req.getCliente());
                    return ResponseEntity.ok(repo.save(m));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}