package petcare.nato.controller;

import petcare.nato.model.Veterinario;
import petcare.nato.repository.VeterinarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarios")
public class VeterinarioController {

    private final VeterinarioRepository repo;

    public VeterinarioController(VeterinarioRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Veterinario> listar() {
        return repo.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Veterinario> crear(@Valid @RequestBody Veterinario req) {
        Veterinario v = repo.save(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(v);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Veterinario> actualizar(@PathVariable Long id, @Valid @RequestBody Veterinario req) {
        return repo.findById(id)
                .map(v -> {
                    v.setNombre(req.getNombre());
                    v.setApellido(req.getApellido());
                    v.setNumeroMatricula(req.getNumeroMatricula());
                    v.setEspecialidad(req.getEspecialidad());
                    v.setTelefono(req.getTelefono());
                    v.setEmail(req.getEmail());
                    return ResponseEntity.ok(repo.save(v));
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
