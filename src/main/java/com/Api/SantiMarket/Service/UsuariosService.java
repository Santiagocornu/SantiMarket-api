package com.Api.SantiMarket.Service;

import com.Api.SantiMarket.Entities.Usuarios;
import com.Api.SantiMarket.Exceptions.BadRequestException;
import com.Api.SantiMarket.Exceptions.ResourceNotFoundException;
import com.Api.SantiMarket.Interfaces.UsuariosInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuariosService {

    private final UsuariosInterface usuariosRepository;

    public UsuariosService(UsuariosInterface usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    // ============================================================
    // GET ALL USERS
    // ============================================================
    /**
     * Obtiene la lista completa de usuarios registrados.
     *
     * @return lista de usuarios
     */
    public List<Usuarios> getAllUsuarios() {
        return usuariosRepository.findAll();
    }

    // ============================================================
    // GET USER BY ID
    // ============================================================
    /**
     * Obtiene un usuario por su ID.
     *
     * @param id identificador del usuario
     * @return usuario encontrado
     * @throws ResourceNotFoundException si no existe el usuario
     */
    public Usuarios getUsuariosById(Integer id) {
        return usuariosRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    // ============================================================
    // CREATE USER
    // ============================================================
    /**
     * Crea un nuevo usuario aplicando validaciones de email único,
     * campos obligatorios y asignación automática de fecha de registro.
     *
     * @param usuario datos del usuario nuevo
     * @return usuario guardado
     * @throws BadRequestException si falta un campo obligatorio
     */
    public Usuarios saveUsuarios(Usuarios usuario) {

        // Validación básica de email
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new BadRequestException("El email es obligatorio.");
        }

        // Validación de contraseña
        if (usuario.getPasswordHash() == null || usuario.getPasswordHash().trim().isEmpty()) {
            throw new BadRequestException("La contraseña es obligatoria.");
        }

        // Verificar email único
        if (usuariosRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new BadRequestException("Ya existe un usuario registrado con ese email.");
        }

        // Asignar fecha de registro si no viene cargada
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(LocalDateTime.now());
        }

        return usuariosRepository.save(usuario);
    }

    // ============================================================
    // UPDATE USER
    // ============================================================
    /**
     * Actualiza los datos de un usuario existente. Solo se modifican los campos
     * enviados por parámetro (actualización parcial).
     *
     * @param id          ID del usuario a actualizar
     * @param nuevosDatos datos nuevos aplicables
     * @return usuario actualizado
     * @throws ResourceNotFoundException si el usuario no existe
     * @throws BadRequestException       si el email está en uso por otro usuario
     */
    public Usuarios updateUsuario(Integer id, Usuarios nuevosDatos) {

        Usuarios existente = usuariosRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        // Validación de email único si se intenta cambiar
        if (nuevosDatos.getEmail() != null && !nuevosDatos.getEmail().trim().isEmpty()) {

            if (!nuevosDatos.getEmail().equalsIgnoreCase(existente.getEmail())) {

                usuariosRepository.findByEmail(nuevosDatos.getEmail())
                        .ifPresent(u -> {
                            throw new BadRequestException("El email ingresado ya está en uso por otro usuario.");
                        });

                existente.setEmail(nuevosDatos.getEmail());
            }
        }

        // Actualización parcial de campos
        if (nuevosDatos.getApodo() != null)
            existente.setApodo(nuevosDatos.getApodo());

        if (nuevosDatos.getPasswordHash() != null && !nuevosDatos.getPasswordHash().trim().isEmpty())
            existente.setPasswordHash(nuevosDatos.getPasswordHash());

        if (nuevosDatos.getDescripcion() != null)
            existente.setDescripcion(nuevosDatos.getDescripcion());

        if (nuevosDatos.getCiudad() != null)
            existente.setCiudad(nuevosDatos.getCiudad());

        if (nuevosDatos.getProvincia() != null)
            existente.setProvincia(nuevosDatos.getProvincia());

        return usuariosRepository.save(existente);
    }

    // ============================================================
    // DELETE USER
    // ============================================================
    /**
     * Elimina un usuario por su ID.
     *
     * @param id identificador del usuario
     * @throws ResourceNotFoundException si el usuario no existe
     */
    public void deleteUsuario(Integer id) {

        if (!usuariosRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: usuario no encontrado con ID: " + id);
        }

        usuariosRepository.deleteById(id);
    }

    // ============================================================
    // LOGIN / VALIDACIÓN DE CREDENCIALES
    // ============================================================
    /**
     * Valida las credenciales del usuario durante el inicio de sesión.
     *
     * @param email    correo del usuario
     * @param password contraseña sin hashear (solo demo)
     * @return usuario autenticado
     * @throws ResourceNotFoundException si el email no existe
     * @throws BadRequestException       si la contraseña es incorrecta
     */
    public Usuarios validarLogin(String email, String password) {

        Usuarios usuario = usuariosRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No existe un usuario con el email proporcionado."));

        if (!usuario.getPasswordHash().equals(password)) {
            throw new BadRequestException("La contraseña ingresada es incorrecta.");
        }

        return usuario;
    }
}
