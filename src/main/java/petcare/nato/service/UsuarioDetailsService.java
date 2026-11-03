package petcare.nato.service;

import petcare.nato.model.Usuario;
import petcare.nato.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Le dice a Spring Security cómo cargar un usuario por su "username" (aquí, el email). */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repo;

    public UsuarioDetailsService(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Usuario u = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe: " + email));
        return User.withUsername(u.getEmail())
                .password(u.getPassword())
                .roles(u.getRol()) // agrega el prefijo ROLE_
                .build();
    }
}
