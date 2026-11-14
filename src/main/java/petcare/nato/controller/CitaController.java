package petcare.nato.controller;

import petcare.nato.model.Cita;
import petcare.nato.repository.CitaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import petcare.nato.service.CitaService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")
public class CitaController {

    private final CitaRepository repo;
    private final CitaService service;

    public CitaController(CitaRepository repo, CitaService service) {
        this.repo = repo;
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'VENDEDOR')")
    public List<Cita> listar() {
        return repo.findAll();
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'VENDEDOR')")
    public ResponseEntity<Cita> crear(@Valid @RequestBody Cita req) {
        Cita c = service.guardar(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'VENDEDOR')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'VENDEDOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
