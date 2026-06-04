package com.mymarket.ms_proveedores.service;

import com.mymarket.ms_proveedores.model.Proveedor;
import com.mymarket.ms_proveedores.repository.ProveedorRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProveedorServiceImpl  implements IProveedorService{

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
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con id: " + id));
    }

    @Override
    public Proveedor guardar(Proveedor proveedor) {
        if(proveedor.getNombre() != null){
            proveedor.setNombre(proveedor.getNombre().toUpperCase());
        }
        return repo.save(proveedor);
    }
    @Override
    public void eliminar(Long id) {
        Proveedor proveedor = buscarPorId(id);
        repo.deleteById(proveedor.getId());
    }

    @Override
    public Proveedor buscarPorRut(String rut) {
        return repo.findByRut(rut)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con rut: " + rut));
    }
}
