package com.mymarket.ms_reportes.service;

import com.mymarket.ms_reportes.model.Reporte;
import com.mymarket.ms_reportes.repository.ReporteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public List<Reporte> obtenerTodos() {
        return reporteRepository.findAll();
    }

    public Reporte crearReporte(Reporte reporte) {
        reporte.setGeneradoEn(LocalDateTime.now());
        Reporte guardado = reporteRepository.save(reporte);
        log.info("Reporte creado con id: {}", guardado.getId());
        return guardado;
    }
}