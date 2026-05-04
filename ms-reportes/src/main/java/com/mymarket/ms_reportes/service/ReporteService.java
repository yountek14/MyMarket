package com.mymarket.ms_reportes.service;

import com.mymarket.ms_reportes.model.Reporte;
import com.mymarket.ms_reportes.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public List<Reporte> obtenerTodos() {
        return reporteRepository.findAll();
    }

    public Reporte crearReporte(Reporte reporte) {
        // Lógica de negocio: Seteamos la fecha de generación antes de guardar
        reporte.setGeneradoEn(LocalDateTime.now());
        return reporteRepository.save(reporte);
    }
}