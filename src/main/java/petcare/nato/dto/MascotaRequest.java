package petcare.nato.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class MascotaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    private String especie;

    private String raza;
    private LocalDate fechaNacimiento;
    private Double peso;
    private String numeroMicrochip;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    public MascotaRequest() {
    }

    public MascotaRequest(String nombre, String especie, String raza, LocalDate fechaNacimiento, Double peso, String numeroMicrochip, Long clienteId) {
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
        this.numeroMicrochip = numeroMicrochip;
        this.clienteId = clienteId;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    public String getNumeroMicrochip() { return numeroMicrochip; }
    public void setNumeroMicrochip(String numeroMicrochip) { this.numeroMicrochip = numeroMicrochip; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}