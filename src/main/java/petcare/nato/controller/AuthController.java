package petcare.nato.controller;

import petcare.nato.dto.AuthResponse;
import petcare.nato.dto.LoginRequest;
import petcare.nato.dto.RegisterRequest;
import petcare.nato.model.Usuario;
import petcare.nato.repository.UsuarioRepository;
import petcare.nato.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository repo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegisterRequest req) {
        if (repo.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }

        Usuario u = new Usuario();
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
       
        u.setRol("CLIENTE");
        
        repo.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarConRol(@Valid @RequestBody RegisterRequest req, @RequestParam String rol) {
        if (repo.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }

        String rolUpper = rol.toUpperCase();
        if (!List.of("ADMIN", "VENDEDOR", "CLIENTE").contains(rolUpper)) {
            return ResponseEntity.badRequest().body("Rol inválido. Use: ADMIN, VENDEDOR o CLIENTE");
        }

        Usuario u = new Usuario();
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRol(rolUpper);

        repo.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        return repo.findByEmail(req.getEmail())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
                .map(u -> {
                    String token = jwtService.generateToken(u.getEmail(), u.getRol()); // pasa email y rol para que quede dentro del token
                    return ResponseEntity.ok(new AuthResponse(token, u.getEmail(), u.getRol()));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado");
        }

        try {
            String oldToken = authHeader.substring(7);
            String email = jwtService.extractEmail(oldToken);

            return repo.findByEmail(email)
                    .map(u -> {
                        String newToken = jwtService.generateToken(u.getEmail(), u.getRol());
                        return ResponseEntity.ok(new AuthResponse(newToken, u.getEmail(), u.getRol()));
                    })
                    .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        }
    }
}