package petcare.nato.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import petcare.nato.dto.CrearOrdenRequest;
import petcare.nato.dto.ItemOrdenRequest;
import petcare.nato.model.Cliente;
import petcare.nato.model.DetalleOrden;
import petcare.nato.model.Orden;
import petcare.nato.model.Servicio;
import petcare.nato.repository.ClienteRepository;
import petcare.nato.repository.OrdenRepository;
import petcare.nato.repository.ServicioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepo;
    private final ClienteRepository clienteRepo;
    private final ServicioRepository servicioRepo;

    public OrdenService(OrdenRepository ordenRepo, ClienteRepository clienteRepo, ServicioRepository servicioRepo) {
        this.ordenRepo = ordenRepo;
        this.clienteRepo = clienteRepo;
        this.servicioRepo = servicioRepo;
    }

    public List<Orden> listarTodas() {
        return ordenRepo.findAll();
    }

    public Orden obtenerPorId(Long id) {
        return ordenRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada"));
    }

    public List<Orden> listarPorCliente(Long clienteId) {
        return ordenRepo.findByClienteId(clienteId);
    }

    @Transactional
    public Orden crearOrden(CrearOrdenRequest req) {
        Cliente cliente = clienteRepo.findById(req.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        Orden orden = new Orden();
        orden.setCliente(cliente);
        orden.setFechaCreacion(LocalDateTime.now());
        orden.setEstado("PENDIENTE");
        orden.setMetodoPago(req.getMetodoPago().toUpperCase());

        double totalCalculado = 0.0;

        for (ItemOrdenRequest itemReq : req.getItems()) {
            Servicio servicio = servicioRepo.findById(itemReq.getServicioId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Servicio no encontrado con ID: " + itemReq.getServicioId()));

            double subtotal = servicio.getPrecio() * itemReq.getCantidad();

            DetalleOrden detalle = new DetalleOrden();
            detalle.setServicio(servicio);
            detalle.setCantidad(itemReq.getCantidad());
            detalle.setPrecioUnitario(servicio.getPrecio());
            detalle.setSubtotal(subtotal);

            orden.agregarItem(detalle);
            totalCalculado += subtotal;
        }

        orden.setTotal(totalCalculado);
        return ordenRepo.save(orden);
    }

    @Transactional
    public Orden confirmarPago(Long id) {
        Orden orden = obtenerPorId(id);
        if ("CANCELADA".equalsIgnoreCase(orden.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede pagar una orden cancelada");
        }
        orden.setEstado("PAGADA");
        return ordenRepo.save(orden);
    }

    @Transactional
    public Orden cancelarOrden(Long id) {
        Orden orden = obtenerPorId(id);
        if ("PAGADA".equalsIgnoreCase(orden.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede cancelar una orden que ya está pagada");
        }
        orden.setEstado("CANCELADA");
        return ordenRepo.save(orden);
    }

    @Transactional
    public Orden actualizarOrden(Long id, CrearOrdenRequest req) {
        Orden orden = obtenerPorId(id);
        if (!"PENDIENTE".equalsIgnoreCase(orden.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden editar órdenes en estado PENDIENTE");
        }

        Cliente cliente = clienteRepo.findById(req.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        orden.setCliente(cliente);
        orden.setMetodoPago(req.getMetodoPago().toUpperCase());
        orden.getItems().clear(); // Limpia los ítems anteriores (orphanRemoval = true los borra de BD)

        double totalCalculado = 0.0;
        for (ItemOrdenRequest itemReq : req.getItems()) {
            Servicio servicio = servicioRepo.findById(itemReq.getServicioId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Servicio no encontrado con ID: " + itemReq.getServicioId()));

            double subtotal = servicio.getPrecio() * itemReq.getCantidad();

            DetalleOrden detalle = new DetalleOrden();
            detalle.setServicio(servicio);
            detalle.setCantidad(itemReq.getCantidad());
            detalle.setPrecioUnitario(servicio.getPrecio());
            detalle.setSubtotal(subtotal);

            orden.agregarItem(detalle);
            totalCalculado += subtotal;
        }

        orden.setTotal(totalCalculado);
        return ordenRepo.save(orden);
    }
}