package petcare.nato.service;

import petcare.nato.model.Usuario;
import petcare.nato.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repo;

    public UsuarioDetailsService(UsuarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        String rol = (usuario.getRol() != null && !usuario.getRol().isBlank())
                ? usuario.getRol().trim().toUpperCase()
                : "CLIENTE";

        String rolConPrefijo = rol.startsWith("ROLE_") ? rol : "ROLE_" + rol;

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(rolConPrefijo));

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                authorities
        );
    }
}