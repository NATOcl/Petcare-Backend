package petcare.nato.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import petcare.nato.dto.CrearOrdenRequest;
import petcare.nato.model.Orden;
import petcare.nato.service.OrdenService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@Tag(name = "Órdenes y Pagos", description = "Gestión de órdenes de compra de servicios y confirmación de pagos")
public class OrdenController {

    private final OrdenService service;

    public OrdenController(OrdenService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'CLIENTE')")
    @Operation(summary = "Listar todas las órdenes")
    public List<Orden> listar() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'CLIENTE')")
    @Operation(summary = "Obtener una orden por ID")
    public ResponseEntity<Orden> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'CLIENTE')")
    @Operation(summary = "Listar órdenes de un cliente específico")
    public List<Orden> listarPorCliente(@PathVariable Long clienteId) {
        return service.listarPorCliente(clienteId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'VENDEDOR')")
    @Operation(summary = "Crear una nueva orden calculando el total automáticamente")
    public ResponseEntity<Orden> crear(@Valid @RequestBody CrearOrdenRequest req) {
        Orden creada = service.crearOrden(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'VENDEDOR')")
    @Operation(summary = "Editar una orden pendiente")
    public ResponseEntity<Orden> actualizar(@PathVariable Long id, @Valid @RequestBody CrearOrdenRequest req) {
        return ResponseEntity.ok(service.actualizarOrden(id, req));
    }

    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'VENDEDOR')")
    @Operation(summary = "Confirmar el pago de una orden pendiente")
    public ResponseEntity<Orden> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmarPago(id));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'VENDEDOR')")
    @Operation(summary = "Cancelar una orden")
    public ResponseEntity<Orden> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelarOrden(id));
    }
}