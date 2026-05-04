package com.mymarket.ms_alertas.service;

import com.mymarket.ms_alertas.dto.InventarioDTO;
import com.mymarket.ms_alertas.dto.ProductoDTO;
import com.mymarket.ms_alertas.model.AlertaModel;
import com.mymarket.ms_alertas.model.EstadoAlerta;
import com.mymarket.ms_alertas.model.TipoAlerta;
import com.mymarket.ms_alertas.repository.AlertaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String INVENTARIO_URL = "http://localhost:8083/api/v1/inventario/";
    private static final String PRODUCTOS_URL = "http://localhost:8082/api/v1/productos/";

    public AlertaService(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    public List<AlertaModel> listarTodas() {
        return alertaRepository.findAll();
    }

    public AlertaModel buscarPorId(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alerta no encontrada con id: " + id));
    }

    public AlertaModel crearAlertaManual(AlertaModel alerta) {

        if (alerta.getActivo() == null) {
            alerta.setActivo(true);
        }

        if (alerta.getFechaCreacion() == null) {
            alerta.setFechaCreacion(LocalDateTime.now());
        }

        if (alerta.getEstadoAlerta() == null) {
            alerta.setEstadoAlerta(EstadoAlerta.PENDIENTE);
        }

        return alertaRepository.save(alerta);
    }

    public AlertaModel resolverAlerta(Long id) {
        AlertaModel alerta = buscarPorId(id);

        alerta.setEstadoAlerta(EstadoAlerta.RESUELTA);
        alerta.setFechaResolucion(LocalDateTime.now());

        return alertaRepository.save(alerta);
    }

    public void eliminarLogico(Long id) {
        AlertaModel alerta = buscarPorId(id);
        alerta.setActivo(false);
        alertaRepository.save(alerta);
    }

    public List<AlertaModel> buscarPorProductoId(Long productoId) {
        return alertaRepository.findByProductoId(productoId);
    }

    public List<AlertaModel> buscarPorInventarioId(Long inventarioId) {
        return alertaRepository.findByInventarioId(inventarioId);
    }

    public List<AlertaModel> buscarPorTipo(TipoAlerta tipoAlerta) {
        return alertaRepository.findByTipoAlerta(tipoAlerta);
    }

    public List<AlertaModel> buscarPorEstado(EstadoAlerta estadoAlerta) {
        return alertaRepository.findByEstadoAlerta(estadoAlerta);
    }

    public List<AlertaModel> buscarPorActivo(Boolean activo) {
        return alertaRepository.findByActivo(activo);
    }

    public AlertaModel generarAlertaStock(Long inventarioId) {

        InventarioDTO inventario = obtenerInventario(inventarioId);
        ProductoDTO producto = obtenerProducto(inventario.getProductoId());

        TipoAlerta tipoAlerta;

        if (inventario.getStockActual() == 0) {
            tipoAlerta = TipoAlerta.STOCK_AGOTADO;
        } else if (inventario.getStockActual() <= inventario.getStockMinimo()) {
            tipoAlerta = TipoAlerta.STOCK_BAJO;
        } else {
            throw new IllegalArgumentException("El inventario no presenta problemas de stock.");
        }

        validarAlertaDuplicada(inventario.getId(), tipoAlerta);

        String mensaje = "Producto " + producto.getNombreProducto()
                + " con lote " + inventario.getLote()
                + " presenta alerta de stock. Stock actual: "
                + inventario.getStockActual()
                + ", stock mínimo: "
                + inventario.getStockMinimo() + ".";

        AlertaModel alerta = AlertaModel.builder()
                .productoId(producto.getId())
                .inventarioId(inventario.getId())
                .tipoAlerta(tipoAlerta)
                .estadoAlerta(EstadoAlerta.ACTIVA)
                .mensaje(mensaje)
                .fechaCreacion(LocalDateTime.now())
                .activo(true)
                .build();

        return alertaRepository.save(alerta);
    }

    public AlertaModel generarAlertaVencimiento(Long inventarioId) {

        InventarioDTO inventario = obtenerInventario(inventarioId);
        ProductoDTO producto = obtenerProducto(inventario.getProductoId());

        LocalDate hoy = LocalDate.now();
        TipoAlerta tipoAlerta;

        if (inventario.getFechaVencimiento().isBefore(hoy)) {
            tipoAlerta = TipoAlerta.PRODUCTO_VENCIDO;
        } else if (!inventario.getFechaVencimiento().isAfter(hoy.plusDays(7))) {
            tipoAlerta = TipoAlerta.PRODUCTO_POR_VENCER;
        } else {
            throw new IllegalArgumentException("El producto no está vencido ni próximo a vencer.");
        }

        validarAlertaDuplicada(inventario.getId(), tipoAlerta);

        String mensaje = "Producto " + producto.getNombreProducto()
                + " con lote " + inventario.getLote()
                + " presenta alerta de vencimiento. Fecha de vencimiento: "
                + inventario.getFechaVencimiento() + ".";

        AlertaModel alerta = AlertaModel.builder()
                .productoId(producto.getId())
                .inventarioId(inventario.getId())
                .tipoAlerta(tipoAlerta)
                .estadoAlerta(EstadoAlerta.ACTIVA)
                .mensaje(mensaje)
                .fechaCreacion(LocalDateTime.now())
                .activo(true)
                .build();

        return alertaRepository.save(alerta);
    }

    private InventarioDTO obtenerInventario(Long inventarioId) {
        try {
            InventarioDTO inventario = restTemplate.getForObject(
                    INVENTARIO_URL + inventarioId,
                    InventarioDTO.class
            );

            if (inventario == null || inventario.getId() == null) {
                throw new IllegalArgumentException("El inventario no existe con id: " + inventarioId);
            }

            return inventario;

        } catch (RestClientException e) {
            throw new IllegalArgumentException("No se pudo obtener el inventario con id: " + inventarioId);
        }
    }

    private ProductoDTO obtenerProducto(Long productoId) {
        try {
            ProductoDTO producto = restTemplate.getForObject(
                    PRODUCTOS_URL + productoId,
                    ProductoDTO.class
            );

            if (producto == null || producto.getId() == null) {
                throw new IllegalArgumentException("El producto no existe con id: " + productoId);
            }

            return producto;

        } catch (RestClientException e) {
            throw new IllegalArgumentException("No se pudo obtener el producto con id: " + productoId);
        }
    }

    private void validarAlertaDuplicada(Long inventarioId, TipoAlerta tipoAlerta) {
        boolean existe = alertaRepository.existsByInventarioIdAndTipoAlertaAndEstadoAlerta(
                inventarioId,
                tipoAlerta,
                EstadoAlerta.ACTIVA
        );

        if (existe) {
            throw new IllegalArgumentException("Ya existe una alerta activa de este tipo para el inventario indicado.");
        }
    }
}