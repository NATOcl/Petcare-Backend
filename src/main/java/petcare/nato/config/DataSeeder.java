package petcare.nato.config;

import petcare.nato.model.Servicio;
import petcare.nato.model.Usuario;
import petcare.nato.repository.ServicioRepository;
import petcare.nato.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(UsuarioRepository usuarios, ServicioRepository servicios, PasswordEncoder enc) {
        return args -> {
            if (usuarios.findByEmail("admin@duoc.cl").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@duoc.cl");
                admin.setPassword(enc.encode("admin123"));
                admin.setRol("ADMIN");
                usuarios.save(admin);
            }
            if (usuarios.findByEmail("user@duoc.cl").isEmpty()) {
                Usuario user = new Usuario();
                user.setEmail("user@duoc.cl");
                user.setPassword(enc.encode("user123"));
                user.setRol("USER");
                usuarios.save(user);
            }
            if (servicios.count() == 0) {
                servicios.save(new Servicio("Consulta General", "Chequeo preventivo de salud", 15000.0));
                servicios.save(new Servicio("Vacunación", "Vacuna anual antiparasitaria y séxtuple", 20000.0));
                servicios.save(new Servicio("Peluquería Canina", "Baño y corte higiénico", 18000.0));
            }
        };
    }
}
