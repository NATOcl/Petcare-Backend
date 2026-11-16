package petcare.nato;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import petcare.nato.model.*;
import petcare.nato.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class NatoApplicationTests {

    private MockMvc mockMvc;

    @Autowired private WebApplicationContext context;
    @Autowired @Qualifier("springSecurityFilterChain") private Filter springSecurityFilterChain;

    private final ObjectMapper objectMapper = new ObjectMapper();
	
    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private ClienteRepository clienteRepo;
    @Autowired private VeterinarioRepository veterinarioRepo;
    @Autowired private ServicioRepository servicioRepo;
    @Autowired private MascotaRepository mascotaRepo;
    @Autowired private CitaRepository citaRepo;
    @Autowired private OrdenRepository ordenRepo;

    private Cliente clienteGuardado;
    private Veterinario vetGuardado;
    private Mascota mascotaGuardada;
    private Servicio servicioGuardado;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();

        ordenRepo.deleteAll();
        citaRepo.deleteAll();
        mascotaRepo.deleteAll();
        servicioRepo.deleteAll();
        veterinarioRepo.deleteAll();
        clienteRepo.deleteAll();
        usuarioRepo.deleteAll();

        // Usuarios con distintos roles
        crearUsuario("admin@petcare.cl", "Admin123!", "ADMIN");
        crearUsuario("vendedor@petcare.cl", "Vendedor123!", "VENDEDOR");
        crearUsuario("cliente@petcare.cl", "Cliente123!", "CLIENTE");

        // Datos base
        Cliente c = new Cliente();
        c.setNombre("Camila");
        c.setApellido("Soto");
        c.setRut("18.456.789-0");
        c.setTelefono("+56912345678");
        c.setEmail("cliente@petcare.cl");
        clienteGuardado = clienteRepo.save(c);

        Veterinario v = new Veterinario();
        v.setNombre("Dr. Matías");
        v.setApellido("Rojas");
        v.setNumeroMatricula("VET-9001");
        v.setEspecialidad("General");
        vetGuardado = veterinarioRepo.save(v);

        Mascota m = new Mascota();
        m.setNombre("Luna");
        m.setEspecie("Perro");
        m.setRaza("Poodle");
        m.setFechaNacimiento(LocalDate.of(2022, 5, 10));
        m.setCliente(clienteGuardado);
        mascotaGuardada = mascotaRepo.save(m);

        Servicio s = new Servicio();
        s.setNombre("Consulta General");
        s.setDescripcion("Revisión médica completa");
        s.setPrecio(25000.0);
        servicioGuardado = servicioRepo.save(s);
    }

    private void crearUsuario(String email, String rawPassword, String rol) {
        Usuario u = new Usuario();
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRol(rol);
        usuarioRepo.save(u);
    }

    private String obtenerToken(String email, String password) throws Exception {
        String body = """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(email, password);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("token").asText();
    }

    @Test
    void accesoSinToken_debeRetornar401() throws Exception {
        mockMvc.perform(get("/api/v1/citas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void vendedorNoPuedeCrearServicio_debeRetornar403() throws Exception {
        String tokenVendedor = obtenerToken("vendedor@petcare.cl", "Vendedor123!");

        String nuevoServicio = """
                {
                  "nombre": "Vacuna Óctuple",
                  "descripcion": "Inmunización anual",
                  "precio": 18000.0
                }
                """;

        mockMvc.perform(post("/api/v1/servicios")
                        .header("Authorization", "Bearer " + tokenVendedor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevoServicio))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminPuedeCrearServicio_debeRetornar201() throws Exception {
        String tokenAdmin = obtenerToken("admin@petcare.cl", "Admin123!");

        String nuevoServicio = """
                {
                  "nombre": "Vacuna Óctuple",
                  "descripcion": "Inmunización anual",
                  "precio": 18000.0
                }
                """;

        mockMvc.perform(post("/api/v1/servicios")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevoServicio))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Vacuna Óctuple"));
    }

    @Test
    void vendedorPuedeAgendarCita_debeRetornar201() throws Exception {
        String tokenVendedor = obtenerToken("vendedor@petcare.cl", "Vendedor123!");

        String nuevaCita = """
                {
                  "fechaHora": "%s",
                  "motivo": "Control anual",
                  "mascota": { "id": %d },
                  "veterinario": { "id": %d }
                }
                """.formatted(LocalDateTime.now().plusDays(2), mascotaGuardada.getId(), vetGuardado.getId());

        mockMvc.perform(post("/api/v1/citas")
                        .header("Authorization", "Bearer " + tokenVendedor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevaCita))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void flujoCompletoOrden_calculaTotalYPaga() throws Exception {
        String tokenVendedor = obtenerToken("vendedor@petcare.cl", "Vendedor123!");

        String nuevaOrden = """
                {
                  "clienteId": %d,
                  "metodoPago": "TARJETA",
                  "items": [
                    { "servicioId": %d, "cantidad": 2 }
                  ]
                }
                """.formatted(clienteGuardado.getId(), servicioGuardado.getId());

        MvcResult resCrear = mockMvc.perform(post("/api/v1/ordenes")
                        .header("Authorization", "Bearer " + tokenVendedor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nuevaOrden))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total").value(50000.0))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andReturn();

        Long ordenId = objectMapper.readTree(resCrear.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(patch("/api/v1/ordenes/" + ordenId + "/pagar")
                        .header("Authorization", "Bearer " + tokenVendedor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PAGADA"));
    }
}