package com.mymarket.ms_productos.service;

import com.mymarket.ms_productos.model.ProductoModel;
import com.mymarket.ms_productos.model.UnidadMedida;
import com.mymarket.ms_productos.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    //Listar todos
    public List<ProductoModel> listarTodos() {
        return productoRepository.findAll();
    }

    //Buscar por ID
    public ProductoModel buscarPorId(Long id) {
        log.info("Buscando producto por id: {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con id: {}", id);
                    return new EntityNotFoundException("Producto no encontrado con id: " + id);
                });
    }

    //Guardar
    public ProductoModel guardar(ProductoModel producto) {

        if (productoRepository.existsByNombreProductoIgnoreCase(producto.getNombreProducto())) {
            log.warn("Intento de crear producto con nombre duplicado: {}", producto.getNombreProducto());
            throw new IllegalArgumentException("Ya existe un producto con el nombre: " + producto.getNombreProducto());
        }

        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }

        ProductoModel guardado = productoRepository.save(producto);
        log.info("Producto creado con id: {}, nombre: {}", guardado.getId(), guardado.getNombreProducto());
        return guardado;
    }

    //Actualizar
    public ProductoModel actualizar(Long id, ProductoModel productoActualizado) {

        ProductoModel productoExistente = buscarPorId(id);

        if (!productoExistente.getNombreProducto().equalsIgnoreCase(productoActualizado.getNombreProducto())
                && productoRepository.existsByNombreProductoIgnoreCase(productoActualizado.getNombreProducto())) {

            log.warn("Intento de actualizar producto con nombre duplicado: {}", productoActualizado.getNombreProducto());
            throw new IllegalArgumentException("Ya existe otro producto con el nombre: "
                    + productoActualizado.getNombreProducto());
        }

        productoExistente.setNombreProducto(productoActualizado.getNombreProducto());
        productoExistente.setCategoria(productoActualizado.getCategoria());
        productoExistente.setUnidadMedida(productoActualizado.getUnidadMedida());
        productoExistente.setPrecioBase(productoActualizado.getPrecioBase());
        productoExistente.setDescripcionProducto(productoActualizado.getDescripcionProducto());
        productoExistente.setFechaCaducidad(productoActualizado.getFechaCaducidad());
        productoExistente.setActivo(productoActualizado.getActivo());

        ProductoModel actualizado = productoRepository.save(productoExistente);
        log.info("Producto actualizado con id: {}", id);
        return actualizado;
    }

    //Eliminación lógica
    public void eliminarLogico(Long id) {
        ProductoModel producto = buscarPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
        log.info("Producto eliminado (logico) con id: {}", id);
    }

    //Filtros
    public List<ProductoModel> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public List<ProductoModel> buscarPorActivo(Boolean activo) {
        return productoRepository.findByActivo(activo);
    }

    public List<ProductoModel> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreProductoContainingIgnoreCase(nombre);
    }

    public List<ProductoModel> buscarPorUnidadMedida(UnidadMedida unidadMedida) {
        return productoRepository.findByUnidadMedida(unidadMedida);
    }
}