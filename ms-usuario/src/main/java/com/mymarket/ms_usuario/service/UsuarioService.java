package com.mymarket.ms_usuario.service;
import com.mymarket.ms_usuario.model.Usuario;
import com.mymarket.ms_usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de gestion de usuarios con eliminacion logica (desactivacion).
 */
@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        log.info("Buscando usuario por id: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con id: {}", id);
                    return new EntityNotFoundException("Usuario no encontrado con id: " + id);
                });
    }

    public Usuario crear(Usuario usuario) {
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario creado con id: {}, email: {}", guardado.getId(), guardado.getEmail());
        return guardado;
    }

    public Usuario actualizar(Long id, Usuario datos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado para actualizar - id: {}", id);
                    return new EntityNotFoundException("Usuario no encontrado con id: " + id);
                });
        usuario.setNombre(datos.getNombre());
        usuario.setEmail(datos.getEmail());
        usuario.setRol(datos.getRol());
        Usuario actualizado = usuarioRepository.save(usuario);
        log.info("Usuario actualizado con id: {}", id);
        return actualizado;
    }

    public void desactivar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado para desactivar - id: {}", id);
                    return new EntityNotFoundException("Usuario no encontrado con id: " + id);
                });
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        log.info("Usuario desactivado con id: {}", id);
    }
}