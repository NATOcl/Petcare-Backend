package petcare.nato.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}[0-9kK]$", message = "Formato de RUT inválido")
    private String rut;

    // El teléfono es opcional enle frontend
    private String telefono;

    @Email(message = "Debe ser un email válido")
    private String email;

    private String direccion;

    public ClienteRequest() {
    }

    public ClienteRequest(String nombre, String apellido, String rut, String telefono, String email, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}