package com.mymarket.ms_alertas.service;

import com.mymarket.ms_alertas.dto.InventarioDTO;
import com.mymarket.ms_alertas.dto.ProductoDTO;
import com.mymarket.ms_alertas.model.AlertaModel;
import com.mymarket.ms_alertas.model.EstadoAlerta;
import com.mymarket.ms_alertas.model.TipoAlerta;
import com.mymarket.ms_alertas.repository.AlertaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de gestion de alertas. Permite crear alertas manuales y generar
 * automaticamente alertas de stock (bajo/agotado) y vencimiento (vencido/por vencer)
 * consultando ms-inventario y ms-productos via WebClient.
 */
@Service
public class AlertaService {

    private static final Logger log = LoggerFactory.getLogger(AlertaService.class);

    private final AlertaRepository alertaRepository;
    private final WebClient webClient;
    private final String inventarioUrl;
    private final String productosUrl;

    public AlertaService(
            AlertaRepository alertaRepository,
            WebClient.Builder webClientBuilder,
            @Value("${api.inventario.url}") String inventarioUrl,
            @Value("${api.productos.url}") String productosUrl) {
        this.alertaRepository = alertaRepository;
        this.webClient = webClientBuilder.build();
        this.inventarioUrl = inventarioUrl;
        this.productosUrl = productosUrl;
    }

    public List<AlertaModel> listarTodas() {
        return alertaRepository.findAll();
    }

    public AlertaModel buscarPorId(Long id) {
        log.info("Buscando alerta por id: {}", id);
        return alertaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Alerta no encontrada con id: {}", id);
                    return new EntityNotFoundException("Alerta no encontrada con id: " + id);
                });
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

        AlertaModel guardada = alertaRepository.save(alerta);
        log.info("Alerta manual creada con id: {}, tipo: {}", guardada.getId(), guardada.getTipoAlerta());
        return guardada;
    }

    public AlertaModel resolverAlerta(Long id) {
        AlertaModel alerta = buscarPorId(id);

        alerta.setEstadoAlerta(EstadoAlerta.RESUELTA);
        alerta.setFechaResolucion(LocalDateTime.now());

        AlertaModel resuelta = alertaRepository.save(alerta);
        log.info("Alerta resuelta con id: {}", id);
        return resuelta;
    }

    public void eliminarLogico(Long id) {
        AlertaModel alerta = buscarPorId(id);
        alerta.setActivo(false);
        alertaRepository.save(alerta);
        log.info("Alerta eliminada (logico) con id: {}", id);
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

    /**
     * Genera alerta de stock automaticamente. Determina si es STOCK_AGOTADO o STOCK_BAJO
     * segun el nivel de stock actual vs el minimo del inventario.
     * Valida que no exista una alerta activa duplicada para el mismo inventario y tipo.
     */
    public AlertaModel generarAlertaStock(Long inventarioId) {
        InventarioDTO inventario = obtenerInventario(inventarioId);
        ProductoDTO producto = obtenerProducto(inventario.getProductoId());

        TipoAlerta tipoAlerta;

        if (inventario.getStockActual() == 0) {
            tipoAlerta = TipoAlerta.STOCK_AGOTADO;
        } else if (inventario.getStockActual() <= inventario.getStockMinimo()) {
            tipoAlerta = TipoAlerta.STOCK_BAJO;
        } else {
            log.warn("Intento de generar alerta de stock sin problemas - inventarioId: {}", inventarioId);
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

        AlertaModel guardada = alertaRepository.save(alerta);
        log.info("Alerta de stock generada - id: {}, tipo: {}, inventarioId: {}", guardada.getId(), guardada.getTipoAlerta(), inventarioId);
        return guardada;
    }

    /**
     * Genera alerta de vencimiento automaticamente. Determina si es PRODUCTO_VENCIDO
     * (fecha anterior a hoy) o PRODUCTO_POR_VENCER (proximos 7 dias).
     */
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
            log.warn("Intento de generar alerta de vencimiento sin problemas - inventarioId: {}", inventarioId);
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

        AlertaModel guardada = alertaRepository.save(alerta);
        log.info("Alerta de vencimiento generada - id: {}, tipo: {}, inventarioId: {}", guardada.getId(), guardada.getTipoAlerta(), inventarioId);
        return guardada;
    }

    private InventarioDTO obtenerInventario(Long inventarioId) {
        try {
            InventarioDTO inventario = webClient.get()
                    .uri(inventarioUrl + inventarioId)
                    .retrieve()
                    .bodyToMono(InventarioDTO.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (inventario == null || inventario.getId() == null) {
                log.warn("Inventario no encontrado con id: {}", inventarioId);
                throw new IllegalArgumentException("El inventario no existe con id: " + inventarioId);
            }

            return inventario;

        } catch (WebClientResponseException.NotFound e) {
            log.warn("Inventario no encontrado via WebClient - id: {}", inventarioId);
            throw new IllegalArgumentException("El inventario no existe con id: " + inventarioId);
        } catch (Exception e) {
            log.error("Error al obtener inventario con id: {}", inventarioId, e);
            throw new IllegalArgumentException("No se pudo obtener el inventario con id: " + inventarioId);
        }
    }

    private ProductoDTO obtenerProducto(Long productoId) {
        try {
            ProductoDTO producto = webClient.get()
                    .uri(productosUrl + productoId)
                    .retrieve()
                    .bodyToMono(ProductoDTO.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (producto == null || producto.getId() == null) {
                log.warn("Producto no encontrado con id: {}", productoId);
                throw new IllegalArgumentException("El producto no existe con id: " + productoId);
            }

            return producto;

        } catch (WebClientResponseException.NotFound e) {
            log.warn("Producto no encontrado via WebClient - id: {}", productoId);
            throw new IllegalArgumentException("El producto no existe con id: " + productoId);
        } catch (Exception e) {
            log.error("Error al obtener producto con id: {}", productoId, e);
            throw new IllegalArgumentException("No se pudo obtener el producto con id: " + productoId);
        }
    }

    /** Evita que se genere mas de una alerta activa del mismo tipo para el mismo inventario. */
    private void validarAlertaDuplicada(Long inventarioId, TipoAlerta tipoAlerta) {
        boolean existe = alertaRepository.existsByInventarioIdAndTipoAlertaAndEstadoAlerta(
                inventarioId,
                tipoAlerta,
                EstadoAlerta.ACTIVA
        );

        if (existe) {
            log.warn("Alerta duplicada - inventarioId: {}, tipo: {}", inventarioId, tipoAlerta);
            throw new IllegalArgumentException("Ya existe una alerta activa de este tipo para el inventario indicado.");
        }
    }
}
