package petcare.nato.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String especie; 

    private String raza;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private Double peso;

    @Column(name = "numero_microchip")
    private String numeroMicrochip;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public Mascota() {
    }

    public Mascota(String nombre, String especie, String raza, LocalDate fechaNacimiento, Double peso, String numeroMicrochip, Cliente cliente) {
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
        this.numeroMicrochip = numeroMicrochip;
        this.cliente = cliente;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}