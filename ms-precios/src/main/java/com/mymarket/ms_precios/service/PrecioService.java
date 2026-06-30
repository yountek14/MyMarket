package com.mymarket.ms_precios.service;

import com.mymarket.ms_precios.dto.PrecioRequestDTO;
import com.mymarket.ms_precios.dto.PrecioResponseDTO;
import com.mymarket.ms_precios.model.Precio;
import com.mymarket.ms_precios.model.Temporada;
import com.mymarket.ms_precios.repository.PrecioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de gestion de precios con soporte de descuentos (porcentaje/fijo)
 * y precios por temporada. El precio final se calcula al devolver el DTO.
 */
@Service
public class PrecioService {

    private static final Logger log = LoggerFactory.getLogger(PrecioService.class);

    private final PrecioRepository precioRepository;

    public PrecioService(PrecioRepository precioRepository) {
        this.precioRepository = precioRepository;
    }

    /**
     * Calcula el precio final aplicando el descuento configurado.
     * PORCENTAJE: descuenta un % del precio base.
     * FIJO: descuenta un monto fijo (minimo $0).
     */
    private Double calcularPrecioFinal(Precio p) {
        if (p.getTipoDescuento() == null || p.getValorDescuento() == null) {
            return p.getPrecioBase();
        }
        return switch (p.getTipoDescuento()) {
            case PORCENTAJE -> p.getPrecioBase() * (1 - p.getValorDescuento() / 100);
            case FIJO -> Math.max(0, p.getPrecioBase() - p.getValorDescuento());
        };
    }

    private Precio toEntity(PrecioRequestDTO dto) {
        Precio p = new Precio();
        p.setProductoId(dto.getProductoId());
        p.setPrecioBase(dto.getPrecioBase());
        p.setTipoDescuento(dto.getTipoDescuento());
        p.setValorDescuento(dto.getValorDescuento());
        p.setTemporada(dto.getTemporada());
        p.setFechaInicio(dto.getFechaInicio());
        p.setFechaFin(dto.getFechaFin());
        return p;
    }

    private PrecioResponseDTO toDTO(Precio p) {
        PrecioResponseDTO dto = new PrecioResponseDTO();
        dto.setId(p.getId());
        dto.setProductoId(p.getProductoId());
        dto.setPrecioBase(p.getPrecioBase());
        dto.setTipoDescuento(p.getTipoDescuento());
        dto.setValorDescuento(p.getValorDescuento());
        dto.setPrecioFinal(calcularPrecioFinal(p)); // se calcula al devolver
        dto.setTemporada(p.getTemporada());
        dto.setFechaInicio(p.getFechaInicio());
        dto.setFechaFin(p.getFechaFin());
        dto.setActivo(p.isActivo());
        return dto;
    }

    public List<PrecioResponseDTO> listarTodos() {
        return precioRepository.findAll().stream().map(this::toDTO).toList();
    }

    public List<PrecioResponseDTO> listarActivos() {
        return precioRepository.findByActivoTrue().stream().map(this::toDTO).toList();
    }

    public List<PrecioResponseDTO> listarPorProducto(Long productoId) {
        return precioRepository.findByProductoId(productoId).stream().map(this::toDTO).toList();
    }

    public List<PrecioResponseDTO> listarPorTemporada(Temporada temporada) {
        return precioRepository.findByTemporada(temporada).stream().map(this::toDTO).toList();
    }

    public PrecioResponseDTO buscarPorId(Long id) {
        log.info("Buscando precio por id: {}", id);
        Precio p = precioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Precio no encontrado con id: {}", id);
                    return new EntityNotFoundException("Precio no encontrado con id: " + id);
                });
        return toDTO(p);
    }

    public PrecioResponseDTO buscarPrecioActualDeProducto(Long productoId) {
        log.info("Buscando precio activo del producto: {}", productoId);
        Precio p = precioRepository
                .findTopByProductoIdAndActivoTrueOrderByFechaInicioDesc(productoId)
                .orElseThrow(() -> {
                    log.warn("No hay precio activo para el producto: {}", productoId);
                    return new EntityNotFoundException("No hay precio activo para el producto con id: " + productoId);
                });
        return toDTO(p);
    }

    public PrecioResponseDTO crear(PrecioRequestDTO dto) {
        PrecioResponseDTO creado = toDTO(precioRepository.save(toEntity(dto)));
        log.info("Precio creado con id: {}, productoId: {}", creado.getId(), creado.getProductoId());
        return creado;
    }

    public PrecioResponseDTO actualizar(Long id, PrecioRequestDTO dto) {
        Precio p = precioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Precio no encontrado para actualizar - id: {}", id);
                    return new EntityNotFoundException("Precio no encontrado con id: " + id);
                });
        p.setPrecioBase(dto.getPrecioBase());
        p.setTipoDescuento(dto.getTipoDescuento());
        p.setValorDescuento(dto.getValorDescuento());
        p.setTemporada(dto.getTemporada());
        p.setFechaInicio(dto.getFechaInicio());
        p.setFechaFin(dto.getFechaFin());
        PrecioResponseDTO actualizado = toDTO(precioRepository.save(p));
        log.info("Precio actualizado con id: {}", id);
        return actualizado;
    }

    public void desactivar(Long id) {
        Precio p = precioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Precio no encontrado para desactivar - id: {}", id);
                    return new EntityNotFoundException("Precio no encontrado con id: " + id);
                });
        p.setActivo(false);
        precioRepository.save(p);
        log.info("Precio desactivado con id: {}", id);
    }
}