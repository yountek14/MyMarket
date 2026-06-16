package com.mymarket.ms_proveedores.service;

import com.mymarket.ms_proveedores.model.Proveedor;
import com.mymarket.ms_proveedores.repository.ProveedorRepo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProveedorServiceImpl  implements IProveedorService{

    private static final Logger log = LoggerFactory.getLogger(ProveedorServiceImpl.class);

    private final ProveedorRepo repo;

    public ProveedorServiceImpl(ProveedorRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Proveedor> listarTodos(){
        return repo.findAll();
    }

    @Override
    public Proveedor buscarPorId(Long id) {
        log.info("Buscando proveedor por id: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Proveedor no encontrado con id: {}", id);
                    return new EntityNotFoundException("Proveedor no encontrado con id: " + id);
                });
    }

    @Override
    public Proveedor guardar(Proveedor proveedor) {
        if(proveedor.getNombre() != null){
            proveedor.setNombre(proveedor.getNombre().toUpperCase());
        }
        Proveedor guardado = repo.save(proveedor);
        log.info("Proveedor creado con id: {}, nombre: {}", guardado.getId(), guardado.getNombre());
        return guardado;
    }
    @Override
    public void eliminar(Long id) {
        Proveedor proveedor = buscarPorId(id);
        repo.deleteById(proveedor.getId());
        log.info("Proveedor eliminado con id: {}", id);
    }

    @Override
    public Proveedor buscarPorRut(String rut) {
        log.info("Buscando proveedor por rut: {}", rut);
        return repo.findByRut(rut)
                .orElseThrow(() -> {
                    log.warn("Proveedor no encontrado con rut: {}", rut);
                    return new EntityNotFoundException("Proveedor no encontrado con rut: " + rut);
                });
    }
}
