package petcare.nato.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private String nombre;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}[0-9kK]$", message = "Formato de RUT inválido (ej: 19011022K)")
    private String rut;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo electrónico válido")
    @Pattern(
        regexp = "^[A-Za-z0-9._%+-]+@(duoc\\.cl|profesor\\.duoc\\.cl|gmail\\.com)$", 
        message = "El correo debe ser @duoc.cl, @profesor.duoc.cl o @gmail.com" 
    )
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 72, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    private String rol;

    public RegisterRequest() {
    }

    public RegisterRequest(String nombre, String rut, String email, String password, String rol) {
        this.nombre = nombre;
        this.rut = rut;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}