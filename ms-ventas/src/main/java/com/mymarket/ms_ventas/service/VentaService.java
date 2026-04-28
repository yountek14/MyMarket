package com.mymarket.ms_ventas.service;

import com.mymarket.ms_ventas.dto.InventarioDTO;
import com.mymarket.ms_ventas.dto.ProductoDTO;
import com.mymarket.ms_ventas.model.EstadoVenta;
import com.mymarket.ms_ventas.model.VentaModel;
import com.mymarket.ms_ventas.repository.VentaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String PRODUCTOS_URL = "http://localhost:8082/api/v1/productos/";
    private static final String INVENTARIO_URL = "http://localhost:8083/api/v1/inventario/";

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public List<VentaModel> listarTodos() {
        return ventaRepository.findAll();
    }

    public VentaModel buscarPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con id: " + id));
    }

    public VentaModel registrarVenta(VentaModel venta) {

        ProductoDTO producto = validarProductoExiste(venta.getProductoId());
        InventarioDTO inventario = validarInventarioExiste(venta.getInventarioId());

        if (!inventario.getProductoId().equals(producto.getId())) {
            throw new IllegalArgumentException("El inventario no corresponde al producto indicado.");
        }

        descontarStock(venta.getInventarioId(), venta.getCantidadVendida());

        if (venta.getPrecioUnitario() == null) {
            venta.setPrecioUnitario(producto.getPrecioBase());
        }

        venta.setTotalVenta(venta.getCantidadVendida() * venta.getPrecioUnitario());
        venta.setFechaVenta(LocalDateTime.now());
        venta.setEstado(EstadoVenta.REGISTRADA);

        if (venta.getActivo() == null) {
            venta.setActivo(true);
        }

        return ventaRepository.save(venta);
    }

    public VentaModel actualizar(Long id, VentaModel ventaActualizada) {
        VentaModel ventaExistente = buscarPorId(id);

        ventaExistente.setProductoId(ventaActualizada.getProductoId());
        ventaExistente.setInventarioId(ventaActualizada.getInventarioId());
        ventaExistente.setCantidadVendida(ventaActualizada.getCantidadVendida());
        ventaExistente.setPrecioUnitario(ventaActualizada.getPrecioUnitario());

        ventaExistente.setTotalVenta(
                ventaActualizada.getCantidadVendida() * ventaActualizada.getPrecioUnitario()
        );

        ventaExistente.setEstado(ventaActualizada.getEstado());
        ventaExistente.setActivo(ventaActualizada.getActivo());

        return ventaRepository.save(ventaExistente);
    }

    public void eliminarLogico(Long id) {
        VentaModel venta = buscarPorId(id);
        venta.setActivo(false);
        ventaRepository.save(venta);
    }

    public VentaModel marcarComoPagada(Long id) {
        VentaModel venta = buscarPorId(id);
        venta.setEstado(EstadoVenta.PAGADA);
        return ventaRepository.save(venta);
    }

    public VentaModel anularVenta(Long id) {
        VentaModel venta = buscarPorId(id);
        venta.setEstado(EstadoVenta.ANULADA);
        return ventaRepository.save(venta);
    }

    public List<VentaModel> buscarPorProductoId(Long productoId) {
        return ventaRepository.findByProductoId(productoId);
    }

    public List<VentaModel> buscarPorInventarioId(Long inventarioId) {
        return ventaRepository.findByInventarioId(inventarioId);
    }

    public List<VentaModel> buscarPorEstado(EstadoVenta estado) {
        return ventaRepository.findByEstado(estado);
    }

    public List<VentaModel> buscarPorActivo(Boolean activo) {
        return ventaRepository.findByActivo(activo);
    }

    public List<VentaModel> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaVentaBetween(inicio, fin);
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

    private InventarioDTO validarInventarioExiste(Long inventarioId) {
        if (inventarioId == null) {
            throw new IllegalArgumentException("El ID del inventario es obligatorio.");
        }

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
            throw new IllegalArgumentException("No se pudo validar el inventario con id: " + inventarioId);
        }
    }

    private void descontarStock(Long inventarioId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad vendida debe ser mayor a 0.");
        }

        try {
            restTemplate.put(
                    INVENTARIO_URL + inventarioId + "/salida?cantidad=" + cantidad,
                    null
            );

        } catch (RestClientException e) {
            throw new IllegalArgumentException("No se pudo descontar stock del inventario. Verifique stock disponible.");
        }
    }
}