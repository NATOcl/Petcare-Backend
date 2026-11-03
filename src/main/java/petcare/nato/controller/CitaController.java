package petcare.nato.controller;

import petcare.nato.model.Cita;
import petcare.nato.repository.CitaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaRepository repo;

    public CitaController(CitaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Cita> listar() {
        return repo.findAll();
    }
    
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Cita> crear(@Valid @RequestBody Cita req) {
        Cita c = repo.save(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Cita> actualizar(@PathVariable Long id, @Valid @RequestBody Cita req) {
        return repo.findById(id)
                .map(c -> {
                    c.setFechaHora(req.getFechaHora());
                    c.setMotivo(req.getMotivo());
                    c.setEstado(req.getEstado());
                    c.setMascota(req.getMascota());
                    c.setVeterinario(req.getVeterinario());
                    return ResponseEntity.ok(repo.save(c));
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
