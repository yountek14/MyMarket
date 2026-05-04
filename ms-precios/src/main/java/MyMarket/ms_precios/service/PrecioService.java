package MyMarket.ms_precios.service;

import MyMarket.ms_precios.dto.PrecioRequestDTO;
import MyMarket.ms_precios.dto.PrecioResponseDTO;
import MyMarket.ms_precios.model.Precio;
import MyMarket.ms_precios.model.Temporada;
import MyMarket.ms_precios.repository.PrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrecioService {

    @Autowired
    private PrecioRepository precioRepository;

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
        Precio p = precioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Precio no encontrado"));
        return toDTO(p);
    }

    public PrecioResponseDTO buscarPrecioActualDeProducto(Long productoId) {
        Precio p = precioRepository
                .findTopByProductoIdAndActivoTrueOrderByFechaInicioDesc(productoId)
                .orElseThrow(() -> new RuntimeException("No hay precio activo para este producto"));
        return toDTO(p);
    }

    public PrecioResponseDTO crear(PrecioRequestDTO dto) {
        return toDTO(precioRepository.save(toEntity(dto)));
    }

    public PrecioResponseDTO actualizar(Long id, PrecioRequestDTO dto) {
        Precio p = precioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Precio no encontrado"));
        p.setPrecioBase(dto.getPrecioBase());
        p.setTipoDescuento(dto.getTipoDescuento());
        p.setValorDescuento(dto.getValorDescuento());
        p.setTemporada(dto.getTemporada());
        p.setFechaInicio(dto.getFechaInicio());
        p.setFechaFin(dto.getFechaFin());
        return toDTO(precioRepository.save(p));
    }

    public void desactivar(Long id) {
        Precio p = precioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Precio no encontrado"));
        p.setActivo(false);
        precioRepository.save(p);
    }
}