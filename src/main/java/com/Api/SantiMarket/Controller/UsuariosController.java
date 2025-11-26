package com.Api.SantiMarket.Controller;

import com.Api.SantiMarket.Entities.CarritoCompras;
import com.Api.SantiMarket.Entities.Usuarios;
import com.Api.SantiMarket.Service.CarritoComprasService;
import com.Api.SantiMarket.Service.UsuariosService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiMarket/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;
    private final CarritoComprasService carritoComprasService;

    public UsuariosController(UsuariosService usuariosService, CarritoComprasService carritoComprasService) {
        this.usuariosService = usuariosService;
        this.carritoComprasService = carritoComprasService;
    }

    // ========================================================================
    // GET - Obtener todos los usuarios
    // ========================================================================
    /**
     * Retorna la lista completa de usuarios registrados en el sistema.
     *
     * @return ResponseEntity con la lista de usuarios.
     */
    @GetMapping
    public ResponseEntity<List<Usuarios>> getAllUsuarios() {
        return ResponseEntity.ok(usuariosService.getAllUsuarios());
    }

    // ========================================================================
    // GET - Buscar usuario por ID
    // ========================================================================
    /**
     * Busca y devuelve un usuario por su identificador único.
     *
     * @param id ID del usuario
     * @return ResponseEntity con el usuario encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> getUsuarioById(@PathVariable Integer id) {
        Usuarios usuario = usuariosService.getUsuariosById(id);
        return ResponseEntity.ok(usuario);
    }

    // ========================================================================
    // POST - Crear usuario
    // ========================================================================
    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param usuario datos del usuario a crear
     * @return ResponseEntity con código 201 y el usuario creado
     */
    @PostMapping
    public ResponseEntity<Usuarios> createUsuario(@RequestBody Usuarios usuario) {

        Usuarios creado = usuariosService.saveUsuarios(usuario);

        return ResponseEntity
                .created(URI.create("/apiMarket/usuarios/" + creado.getId()))
                .body(creado);
    }

    // ========================================================================
    // PUT - Actualizar usuario por ID
    // ========================================================================
    /**
     * Actualiza la información de un usuario ya existente.
     *
     * @param id ID del usuario a actualizar
     * @param usuarioActualizado nuevos datos del usuario
     * @return ResponseEntity con el usuario actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuarios> updateUsuario(
            @PathVariable Integer id,
            @RequestBody Usuarios usuarioActualizado) {

        Usuarios usuario = usuariosService.updateUsuario(id, usuarioActualizado);
        return ResponseEntity.ok(usuario);
    }

    // ========================================================================
    // DELETE - Eliminar usuario por ID
    // ========================================================================
    /**
     * Elimina un usuario del sistema por su ID.
     *
     * @param id ID del usuario a eliminar
     * @return ResponseEntity sin contenido (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        usuariosService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // ========================================================================
    // POST - Crear usuario con carrito asignado automáticamente
    // ========================================================================
    /**
     * Crea un usuario y simultáneamente genera un carrito vacío asignado a él.
     *
     * @param usuario datos del nuevo usuario
     * @return ResponseEntity con el usuario y el carrito creados
     */
    @PostMapping("/createUsuarioWithCarrito")
    @Transactional
    public ResponseEntity<?> createUsuarioWithCarrito(@RequestBody Usuarios usuario) {

        // 1. Crear usuario (solo una vez)
        Usuarios nuevoUsuario = usuariosService.saveUsuarios(usuario);

        // 2. Crear carrito vacío
        CarritoCompras carrito = new CarritoCompras();
        carrito.setUsuario(nuevoUsuario);

        CarritoCompras carritoCreado = carritoComprasService.saveCarrito(carrito);

        // 3. Asociar carrito (JPA sincroniza automáticamente al final del @Transactional)
        nuevoUsuario.setCarrito(carritoCreado);

        return ResponseEntity.ok(
                Map.of(
                        "usuario", nuevoUsuario,
                        "carrito", carritoCreado
                )
        );
    }


    // ========================================================================
    // POST - Login
    // ========================================================================
    /**
     * Valida las credenciales del usuario y retorna sus datos
     * si el inicio de sesión es exitoso.
     *
     * @param loginData contiene email y password en formato JSON
     * @return ResponseEntity con el usuario autenticado o error 401
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {

        String email = loginData.get("email");
        String password = loginData.get("password");

        // Validación mínima de entrada
        if (email == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email y contraseña son obligatorios"));
        }

        try {
            Usuarios usuario = usuariosService.validarLogin(email, password);
            return ResponseEntity.ok(usuario);

        } catch (RuntimeException e) {
            // Respuesta estándar para credenciales inválidas
            return ResponseEntity.status(401)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
