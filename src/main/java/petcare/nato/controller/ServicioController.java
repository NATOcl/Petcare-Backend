package petcare.nato.controller;

import petcare.nato.model.Servicio;
import petcare.nato.repository.ServicioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/servicios")
public class ServicioController {

    private final ServicioRepository repo;

    public ServicioController(ServicioRepository repo) {
        this.repo = repo;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public List<Servicio> listar() {
        return repo.findAll();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<Servicio> obtenerPorId(@PathVariable Long id) { 
        return repo.findById(id)
            .map(servicio -> ResponseEntity.ok(servicio))
            .orElse(ResponseEntity.notFound().build());
     }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Servicio> crear(@Valid @RequestBody Servicio req) {
        Servicio s = repo.save(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Servicio> actualizar(@PathVariable Long id, @Valid @RequestBody Servicio req) {
        return repo.findById(id)
                .map(s -> {
                    s.setNombre(req.getNombre());
                    s.setDescripcion(req.getDescripcion());
                    s.setPrecio(req.getPrecio());
                    return ResponseEntity.ok(repo.save(s));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
