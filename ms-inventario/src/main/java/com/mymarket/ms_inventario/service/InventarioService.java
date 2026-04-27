package com.mymarket.ms_inventario.service;

import com.mymarket.ms_inventario.dto.ProductoDTO;
import com.mymarket.ms_inventario.model.EstadoInventario;
import com.mymarket.ms_inventario.model.InventarioModel;
import com.mymarket.ms_inventario.repository.InventarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String PRODUCTOS_URL = "http://localhost:8082/api/v1/productos/";

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public List<InventarioModel> listarTodos() {
        return inventarioRepository.findAll();
    }

    public InventarioModel buscarPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado con id: " + id));
    }

    public InventarioModel guardar(InventarioModel inventario) {

        validarProductoExiste(inventario.getProductoId());

        if (inventarioRepository.existsByLote(inventario.getLote())) {
            throw new IllegalArgumentException("Ya existe un inventario registrado con el lote: " + inventario.getLote());
        }

        if (inventario.getActivo() == null) {
            inventario.setActivo(true);
        }

        actualizarEstadoAutomatico(inventario);

        return inventarioRepository.save(inventario);
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

        return inventarioRepository.save(inventarioExistente);
    }

    public void eliminarLogico(Long id) {
        InventarioModel inventario = buscarPorId(id);
        inventario.setActivo(false);
        inventarioRepository.save(inventario);
    }

    public InventarioModel registrarEntrada(Long id, Integer cantidad) {

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de entrada debe ser mayor a 0.");
        }

        InventarioModel inventario = buscarPorId(id);
        inventario.setStockActual(inventario.getStockActual() + cantidad);

        actualizarEstadoAutomatico(inventario);

        return inventarioRepository.save(inventario);
    }

    public InventarioModel registrarSalida(Long id, Integer cantidad) {

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de salida debe ser mayor a 0.");
        }

        InventarioModel inventario = buscarPorId(id);

        if (inventario.getStockActual() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + inventario.getStockActual());
        }

        inventario.setStockActual(inventario.getStockActual() - cantidad);

        actualizarEstadoAutomatico(inventario);

        return inventarioRepository.save(inventario);
    }

    public InventarioModel registrarMerma(Long id, Integer cantidad) {

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de merma debe ser mayor a 0.");
        }

        InventarioModel inventario = buscarPorId(id);

        if (inventario.getStockActual() < cantidad) {
            throw new IllegalArgumentException("No se puede registrar merma mayor al stock actual.");
        }

        inventario.setMerma(inventario.getMerma() + cantidad);
        inventario.setStockActual(inventario.getStockActual() - cantidad);

        actualizarEstadoAutomatico(inventario);

        return inventarioRepository.save(inventario);
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
            throw new IllegalArgumentException("El límite de stock no puede ser negativo.");
        }

        return inventarioRepository.findByStockActualLessThanEqual(stockLimite);
    }

    private ProductoDTO validarProductoExiste(Long productoId) {

        if (productoId == null) {
            throw new IllegalArgumentException("El ID del producto es obligatorio.");
        }

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