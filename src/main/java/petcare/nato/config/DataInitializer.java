package petcare.nato.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import petcare.nato.model.Cliente;
import petcare.nato.model.Mascota;
import petcare.nato.model.Usuario;
import petcare.nato.model.Veterinario;
import petcare.nato.repository.ClienteRepository;
import petcare.nato.repository.MascotaRepository;
import petcare.nato.repository.UsuarioRepository;
import petcare.nato.repository.VeterinarioRepository;

import java.time.LocalDate;

@Component
@Profile("!test") // Evita interferir con el test 
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final ClienteRepository clienteRepo;
    private final VeterinarioRepository veterinarioRepo;
    private final MascotaRepository mascotaRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepo,
                           ClienteRepository clienteRepo,
                           VeterinarioRepository veterinarioRepo,
                           MascotaRepository mascotaRepo,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.clienteRepo = clienteRepo;
        this.veterinarioRepo = veterinarioRepo;
        this.mascotaRepo = mascotaRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 1. Usuarios por defecto para cada rol asi verifiar que funciona 
        crearUsuarioSiNoExiste("admin@petcare.cl", "Admin123!", "ADMIN");
        crearUsuarioSiNoExiste("vendedor@petcare.cl", "Vendedor123!", "VENDEDOR");
        crearUsuarioSiNoExiste("cliente@petcare.cl", "Cliente123!", "CLIENTE");

        // 2. Ficha de Cliente vinculada al usuario cliente@petcare.cl + Mascota inicial
        if (clienteRepo.count() == 0) {
            Cliente cliente = new Cliente();
            cliente.setNombre("Camila");
            cliente.setApellido("Soto");
            cliente.setRut("18.456.789-0");
            cliente.setTelefono("+56912345678");
            cliente.setEmail("cliente@petcare.cl");
            cliente.setDireccion("Av. Macul 2450, Santiago");
            Cliente clienteGuardado = clienteRepo.save(cliente);

            Mascota mascota = new Mascota();
            mascota.setNombre("Luna");
            mascota.setEspecie("Perro");
            mascota.setRaza("Poodle");
            mascota.setFechaNacimiento(LocalDate.of(2022, 5, 10));
            mascota.setPeso(6.5);
            mascota.setNumeroMicrochip("981020000123456");
            mascota.setCliente(clienteGuardado);
            mascotaRepo.save(mascota);
        }

        // 3. Veterinario inicial 
        if (veterinarioRepo.count() == 0) {
            Veterinario vet = new Veterinario();
            vet.setNombre("Matías");
            vet.setApellido("Rojas");
            vet.setNumeroMatricula("VET-9001");
            vet.setEspecialidad("Medicina General");
            vet.setTelefono("+56987654321");
            vet.setEmail("m.rojas@petcare.cl");
            veterinarioRepo.save(vet);
        }

        System.out.println(">>> Datos iniciales y usuarios por defecto verificados correctamente.");
    }

    private void crearUsuarioSiNoExiste(String email, String rawPassword, String rol) {
        if (usuarioRepo.findByEmail(email).isEmpty()) {
            Usuario u = new Usuario();
            u.setEmail(email);
            u.setPassword(passwordEncoder.encode(rawPassword));
            u.setRol(rol);
            usuarioRepo.save(u);
        }
    }
}