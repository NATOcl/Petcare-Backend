package petcare.nato.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CitaRequest {

    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime fechaHora;

    private String motivo;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long mascotaId;

    @NotNull(message = "El ID del veterinario es obligatorio")
    private Long veterinarioId;

    public CitaRequest() {
    }

    public CitaRequest(LocalDateTime fechaHora, String motivo, String estado, Long mascotaId, Long veterinarioId) {
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.estado = estado;
        this.mascotaId = mascotaId;
        this.veterinarioId = veterinarioId;
    }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Long getMascotaId() { return mascotaId; }
    public void setMascotaId(Long mascotaId) { this.mascotaId = mascotaId; }
    public Long getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Long veterinarioId) { this.veterinarioId = veterinarioId; }
}