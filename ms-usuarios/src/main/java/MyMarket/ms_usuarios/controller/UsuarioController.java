package MyMarket.ms_usuarios.controller;

import MyMarket.ms_usuarios.model.Usuario;
import MyMarket.ms_usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listar(){
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id){
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crear(usuario));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario){
        return ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id){
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
