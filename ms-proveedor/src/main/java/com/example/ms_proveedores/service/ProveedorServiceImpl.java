package com.example.ms_proveedores.service;

import com.example.ms_proveedores.model.Proveedor;
import com.example.ms_proveedores.repository.ProveedorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProveedorServiceImpl  implements IProveedorService{

    @Autowired
    private ProveedorRepo repo;

    @Override
    public List<Proveedor> listarTodos(){
        return repo.findAll();
    }

    @Override
    public Proveedor buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
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
        repo.deleteById(id);
    }

    @Override
    public Proveedor buscarPorRut(String rut) {
        return repo.findByRut(rut).orElse(null);
    }
}
