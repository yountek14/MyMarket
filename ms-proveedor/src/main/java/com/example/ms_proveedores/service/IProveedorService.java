package com.example.ms_proveedores.service;

import com.example.ms_proveedores.model.Proveedor;

import java.util.List;

public interface IProveedorService {
    List<Proveedor> listarTodos();
    Proveedor buscarPorId(Long id);
    Proveedor guardar(Proveedor proveedor);
    void eliminar (Long id);

    Proveedor buscarPorRut(String rut);
}
