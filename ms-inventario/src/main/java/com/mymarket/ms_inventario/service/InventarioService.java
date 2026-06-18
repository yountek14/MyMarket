package com.mymarket.ms_inventario.service;

import com.mymarket.ms_inventario.dto.ProductoDTO;
import com.mymarket.ms_inventario.model.EstadoInventario;
import com.mymarket.ms_inventario.model.InventarioModel;
import com.mymarket.ms_inventario.repository.InventarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class InventarioService {

    private static final Logger log = LoggerFactory.getLogger(InventarioService.class);

    private final InventarioRepository inventarioRepository;
    private final WebClient webClient;
    private final String productosUrl;

    public InventarioService(
            InventarioRepository inventarioRepository,
            WebClient.Builder webClientBuilder,
            @Value("${api.productos.url}") String productosUrl) {
        this.inventarioRepository = inventarioRepository;
        this.webClient = webClientBuilder.build();
        this.productosUrl = productosUrl;
    }

    public List<InventarioModel> listarTodos() {
        return inventarioRepository.findAll();
    }

    public InventarioModel buscarPorId(Long id) {
        log.info("Buscando inventario por id: {}", id);
        return inventarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Inventario no encontrado con id: {}", id);
                    return new EntityNotFoundException("Inventario no encontrado con id: " + id);
                });
    }

    public InventarioModel guardar(InventarioModel inventario) {
        validarProductoExiste(inventario.getProductoId());

        if (inventarioRepository.existsByLote(inventario.getLote())) {
            log.warn("Intento de crear inventario con lote duplicado: {}", inventario.getLote());
            throw new IllegalArgumentException("Ya existe un inventario registrado con el lote: " + inventario.getLote());
        }

        if (inventario.getActivo() == null) {
            inventario.setActivo(true);
        }

        actualizarEstadoAutomatico(inventario);

        InventarioModel guardado = inventarioRepository.save(inventario);
        log.info("Inventario creado con id: {}, lote: {}", guardado.getId(), guardado.getLote());
        return guardado;
    }

    public InventarioModel actualizar(Long id, InventarioModel inventarioActualizado) {
        validarProductoExiste(inventarioActualizado.getProductoId());

        InventarioModel inventarioExistente = buscarPorId(id);

        inventarioExistente.setProductoId(inventarioActualizado.getProductoId());
        inventarioExistente.setLote(inventarioActualizado.getLote());
        inventarioExistente.setStockActual(inventarioActualizado.getStockActual());
        inventarioExistente.setStockMinimo(inventarioActualizado.getStockMinimo());
        inventarioExistente.setStockMaximo(inventarioActualizado.getStockMaximo());
        inventarioExistente.setMerma(inventarioActualizado.getMerma());
        inventarioExistente.setFechaIngreso(inventarioActualizado.getFechaIngreso());
        inventarioExistente.setFechaVencimiento(inventarioActualizado.getFechaVencimiento());
        inventarioExistente.setActivo(inventarioActualizado.getActivo());

        actualizarEstadoAutomatico(inventarioExistente);

        InventarioModel actualizado = inventarioRepository.save(inventarioExistente);
        log.info("Inventario actualizado con id: {}", id);
        return actualizado;
    }

    public void eliminarLogico(Long id) {
        InventarioModel inventario = buscarPorId(id);
        inventario.setActivo(false);
        inventarioRepository.save(inventario);
        log.info("Inventario eliminado (logico) con id: {}", id);
    }

    public InventarioModel registrarEntrada(Long id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            log.warn("Cantidad de entrada invalida: {} para inventario id: {}", cantidad, id);
            throw new IllegalArgumentException("La cantidad de entrada debe ser mayor a 0.");
        }

        InventarioModel inventario = buscarPorId(id);
        inventario.setStockActual(inventario.getStockActual() + cantidad);

        actualizarEstadoAutomatico(inventario);

        InventarioModel actualizado = inventarioRepository.save(inventario);
        log.info("Entrada registrada - inventarioId: {}, cantidad: {}, stockActual: {}", id, cantidad, actualizado.getStockActual());
        return actualizado;
    }

    public InventarioModel registrarSalida(Long id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            log.warn("Cantidad de salida invalida: {} para inventario id: {}", cantidad, id);
            throw new IllegalArgumentException("La cantidad de salida debe ser mayor a 0.");
        }

        InventarioModel inventario = buscarPorId(id);

        if (inventario.getStockActual() < cantidad) {
            log.warn("Stock insuficiente - inventarioId: {}, stockActual: {}, solicitado: {}", id, inventario.getStockActual(), cantidad);
            throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + inventario.getStockActual());
        }

        inventario.setStockActual(inventario.getStockActual() - cantidad);

        actualizarEstadoAutomatico(inventario);

        InventarioModel actualizado = inventarioRepository.save(inventario);
        log.info("Salida registrada - inventarioId: {}, cantidad: {}, stockActual: {}", id, cantidad, actualizado.getStockActual());
        return actualizado;
    }

    public InventarioModel registrarMerma(Long id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            log.warn("Cantidad de merma invalida: {} para inventario id: {}", cantidad, id);
            throw new IllegalArgumentException("La cantidad de merma debe ser mayor a 0.");
        }

        InventarioModel inventario = buscarPorId(id);

        if (inventario.getStockActual() < cantidad) {
            log.warn("Merma excede stock - inventarioId: {}, stockActual: {}, merma: {}", id, inventario.getStockActual(), cantidad);
            throw new IllegalArgumentException("No se puede registrar merma mayor al stock actual.");
        }

        inventario.setMerma(inventario.getMerma() + cantidad);
        inventario.setStockActual(inventario.getStockActual() - cantidad);

        actualizarEstadoAutomatico(inventario);

        InventarioModel actualizado = inventarioRepository.save(inventario);
        log.info("Merma registrada - inventarioId: {}, cantidad: {}, mermaTotal: {}, stockActual: {}", id, cantidad, actualizado.getMerma(), actualizado.getStockActual());
        return actualizado;
    }

    public List<InventarioModel> buscarPorProductoId(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    public List<InventarioModel> buscarPorLote(String lote) {
        return inventarioRepository.findByLote(lote);
    }

    public List<InventarioModel> buscarPorEstado(EstadoInventario estado) {
        return inventarioRepository.findByEstado(estado);
    }

    public List<InventarioModel> buscarPorActivo(Boolean activo) {
        return inventarioRepository.findByActivo(activo);
    }

    public List<InventarioModel> buscarVencidos() {
        return inventarioRepository.findByFechaVencimientoBefore(LocalDate.now());
    }

    public List<InventarioModel> buscarPorVencer(Integer dias) {
        if (dias == null || dias <= 0) {
            dias = 7;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate fechaLimite = hoy.plusDays(dias);

        return inventarioRepository.findByFechaVencimientoBetween(hoy, fechaLimite);
    }

    public List<InventarioModel> buscarStockBajo(Integer stockLimite) {
        if (stockLimite == null || stockLimite < 0) {
            log.warn("Limite de stock invalido: {}", stockLimite);
            throw new IllegalArgumentException("El límite de stock no puede ser negativo.");
        }

        return inventarioRepository.findByStockActualLessThanEqual(stockLimite);
    }

    private ProductoDTO validarProductoExiste(Long productoId) {
        if (productoId == null) {
            log.warn("ID de producto es null");
            throw new IllegalArgumentException("El ID del producto es obligatorio.");
        }

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
            log.error("Error al validar producto con id: {}", productoId, e);
            throw new IllegalArgumentException("No se pudo validar el producto con id: " + productoId);
        }
    }

    private void actualizarEstadoAutomatico(InventarioModel inventario) {
        if (inventario.getFechaVencimiento().isBefore(LocalDate.now())) {
            inventario.setEstado(EstadoInventario.VENCIDO);
        } else if (inventario.getStockActual() == 0) {
            inventario.setEstado(EstadoInventario.AGOTADO);
        } else if (inventario.getStockActual() <= inventario.getStockMinimo()) {
            inventario.setEstado(EstadoInventario.BAJO_STOCK);
        } else {
            inventario.setEstado(EstadoInventario.DISPONIBLE);
        }
    }
}
