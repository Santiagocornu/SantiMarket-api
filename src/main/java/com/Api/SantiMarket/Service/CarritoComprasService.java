package com.Api.SantiMarket.Service;

import com.Api.SantiMarket.Entities.CarritoCompras;
import com.Api.SantiMarket.Entities.Usuarios;
import com.Api.SantiMarket.Exceptions.BadRequestException;
import com.Api.SantiMarket.Exceptions.ResourceNotFoundException;
import com.Api.SantiMarket.Interfaces.CarritoComprasInteraface;
import com.Api.SantiMarket.Interfaces.UsuariosInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoComprasService {

    private final CarritoComprasInteraface carritoComprasRepository;
    private final UsuariosInterface usuariosRepository;

    public CarritoComprasService(CarritoComprasInteraface carritoComprasRepository,
                                 UsuariosInterface usuariosRepository) {
        this.carritoComprasRepository = carritoComprasRepository;
        this.usuariosRepository = usuariosRepository;
    }

    // ============================================================
    // GET ALL CARRITOS
    // ============================================================
    /**
     * Obtiene la lista completa de carritos de compras registrados.
     *
     * @return lista de carritos
     */
    public List<CarritoCompras> getAllCarritos() {
        return carritoComprasRepository.findAll();
    }

    // ============================================================
    // GET CARRITO BY ID
    // ============================================================
    /**
     * Obtiene un carrito de compras por su ID.
     *
     * @param id identificador del carrito
     * @return carrito encontrado
     * @throws ResourceNotFoundException si no existe el carrito
     */
    public CarritoCompras getCarritoById(Integer id) {
        return carritoComprasRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Carrito no encontrado con ID: " + id));
    }

    // ============================================================
    // GET CARRITO BY USUARIO ID
    // ============================================================
    /**
     * Obtiene el carrito de compras asociado a un usuario por su ID.
     *
     * @param usuarioId identificador del usuario
     * @return carrito del usuario
     * @throws ResourceNotFoundException si el usuario no tiene carrito
     */
    public CarritoCompras getCarritoByUsuarioId(Integer usuarioId) {
        CarritoCompras carrito = carritoComprasRepository.findByUsuarioId(usuarioId);

        if (carrito == null) {
            throw new ResourceNotFoundException("El usuario con ID " + usuarioId + " no tiene carrito.");
        }

        return carrito;
    }

    // ============================================================
    // CREATE CARRITO
    // ============================================================
    /**
     * Crea un nuevo carrito de compras aplicando validaciones básicas.
     *
     * @param carritoCompras datos del carrito nuevo
     * @return carrito guardado
     * @throws BadRequestException si el usuario es nulo
     */
    public CarritoCompras saveCarrito(CarritoCompras carritoCompras) {
        if (carritoCompras.getUsuario() == null) {
            throw new BadRequestException("El usuario no puede ser nulo.");
        }

        return carritoComprasRepository.save(carritoCompras);
    }

    // ============================================================
    // UPDATE CARRITO
    // ============================================================
    /**
     * Actualiza los datos de un carrito existente. Solo se modifica el total.
     *
     * @param id             ID del carrito a actualizar
     * @param carritoCompras datos nuevos aplicables
     * @return carrito actualizado
     * @throws ResourceNotFoundException si el carrito no existe
     */
    public CarritoCompras updateCarrito(Integer id, CarritoCompras carritoCompras) {
        CarritoCompras existente = carritoComprasRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Carrito no encontrado con ID: " + id));

        existente.setTotal(carritoCompras.getTotal());

        return carritoComprasRepository.save(existente);
    }

    // ============================================================
    // ASIGNAR USUARIO A CARRITO
    // ============================================================
    /**
     * Asigna un usuario a un carrito existente.
     *
     * @param carritoId ID del carrito
     * @param usuarioId ID del usuario
     * @return carrito actualizado con usuario asignado
     * @throws ResourceNotFoundException si el carrito o usuario no existen
     */
    public CarritoCompras asignarUsuario(Integer carritoId, Integer usuarioId) {
        // 1. Buscar carrito
        CarritoCompras carrito = carritoComprasRepository.findById(carritoId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con ID: " + carritoId));

        // 2. Buscar usuario
        Usuarios usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        // 3. Asignar usuario
        carrito.setUsuario(usuario);

        // 4. Guardar
        return carritoComprasRepository.save(carrito);
    }

    // ============================================================
    // DELETE CARRITO
    // ============================================================
    /**
     * Elimina un carrito de compras por su ID.
     *
     * @param id identificador del carrito
     * @throws ResourceNotFoundException si el carrito no existe
     */
    public void deleteCarrito(Integer id) {
        if (!carritoComprasRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: carrito no encontrado con ID: " + id);
        }

        // Borra el carrito
        carritoComprasRepository.deleteById(id);
    }

    // ============================================================
    // UPDATE TOTAL CARRITO
    // ============================================================
    /**
     * Actualiza solo el total de un carrito existente.
     *
     * @param id         ID del carrito
     * @param nuevoTotal nuevo valor del total
     * @return carrito actualizado
     * @throws ResourceNotFoundException si el carrito no existe
     */
    public CarritoCompras updateTotal(Integer id, Double nuevoTotal) {
        CarritoCompras existente = carritoComprasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado con ID: " + id));

        existente.setTotal(nuevoTotal);

        return carritoComprasRepository.save(existente);
    }
}
