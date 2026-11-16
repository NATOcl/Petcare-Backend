CREATE TABLE ordenes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_creacion TIMESTAMP NOT NULL,
    estado VARCHAR(255) NOT NULL,
    metodo_pago VARCHAR(255),
    total DOUBLE NOT NULL,
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_orden_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE detalle_ordenes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    orden_id BIGINT NOT NULL,
    servicio_id BIGINT NOT NULL,
    CONSTRAINT fk_detalle_orden FOREIGN KEY (orden_id) REFERENCES ordenes(id),
    CONSTRAINT fk_detalle_servicio FOREIGN KEY (servicio_id) REFERENCES servicios(id)
);