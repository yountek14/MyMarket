package com.mymarket.ms_usuario.controller;

import com.mymarket.ms_usuario.model.Usuario;
import com.mymarket.ms_usuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestion de usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Obtiene todos los usuarios", description = "Retorna la lista completa de usuarios registrados")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping
    public ResponseEntity<List<Usuario>> listar(){
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @Operation(summary = "Busca usuario por ID", description = "Retorna un usuario especifico segun su ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usuario encontrado"), @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Operation(summary = "Crea un usuario", description = "Permite registrar un nuevo usuario en el sistema")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Usuario creado"), @ApiResponse(responseCode = "400", description = "Datos invalidos")})
    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crear(usuario));
    }

    @Operation(summary = "Actualiza un usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usuario actualizado"), @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario){
        return ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }

    @Operation(summary = "Desactiva un usuario", description = "Realiza una desactivacion logica de un usuario")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Usuario desactivado"), @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id){
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
