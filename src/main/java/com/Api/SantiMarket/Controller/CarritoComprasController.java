package com.Api.SantiMarket.Controller;

import com.Api.SantiMarket.Entities.CarritoCompras;
import com.Api.SantiMarket.Service.CarritoComprasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiMarket/carrito")
public class CarritoComprasController {

    private final CarritoComprasService carritoService;

    public CarritoComprasController(CarritoComprasService carritoService) {
        this.carritoService = carritoService;
    }

    // ============================================================
    // GET ALL CARRITOS
    // ============================================================
    /**
     * Obtiene la lista completa de carritos de compras.
     *
     * @return ResponseEntity con lista de carritos
     */
    @GetMapping
    public ResponseEntity<List<CarritoCompras>> getAllCarritos() {
        return ResponseEntity.ok(carritoService.getAllCarritos());
    }

    // ============================================================
    // GET CARRITO BY ID
    // ============================================================
    /**
     * Obtiene un carrito de compras por su ID.
     *
     * @param id ID del carrito
     * @return ResponseEntity con el carrito encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarritoCompras> getCarritoById(@PathVariable Integer id) {
        return ResponseEntity.ok(carritoService.getCarritoById(id));
    }

    // ============================================================
    // GET CARRITO BY USUARIO ID
    // ============================================================
    /**
     * Obtiene el carrito de compras asociado a un usuario por su ID.
     *
     * @param usuarioId ID del usuario
     * @return ResponseEntity con el carrito del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarritoCompras> getCarritoByUsuarioId(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(carritoService.getCarritoByUsuarioId(usuarioId));
    }

    // ============================================================
    // CREATE CARRITO
    // ============================================================
    /**
     * Crea un nuevo carrito de compras.
     *
     * @param carrito datos del carrito a crear
     * @return ResponseEntity con el carrito creado
     */
    @PostMapping
    public ResponseEntity<CarritoCompras> createCarrito(@RequestBody CarritoCompras carrito) {
        CarritoCompras creado = carritoService.saveCarrito(carrito);
        return ResponseEntity.ok(creado);
    }

    // ============================================================
    // UPDATE CARRITO
    // ============================================================
    /**
     * Actualiza los datos de un carrito existente.
     *
     * @param id       ID del carrito a actualizar
     * @param carrito  datos nuevos del carrito
     * @return ResponseEntity con el carrito actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarritoCompras> updateCarrito(
            @PathVariable Integer id,
            @RequestBody CarritoCompras carrito) {

        CarritoCompras actualizado = carritoService.updateCarrito(id, carrito);
        return ResponseEntity.ok(actualizado);
    }

    // ============================================================
    // ASIGNAR USUARIO A CARRITO
    // ============================================================
    /**
     * Asigna un usuario a un carrito existente.
     *
     * @param carritoId ID del carrito
     * @param usuarioId ID del usuario
     * @return ResponseEntity con el carrito actualizado
     */
    @PutMapping("/{carritoId}/asignarUsuario/{usuarioId}")
    public ResponseEntity<CarritoCompras> asignarUsuario(
            @PathVariable Integer carritoId,
            @PathVariable Integer usuarioId) {

        CarritoCompras actualizado = carritoService.asignarUsuario(carritoId, usuarioId);
        return ResponseEntity.ok(actualizado);
    }

    // ============================================================
    // DELETE CARRITO
    // ============================================================
    /**
     * Elimina un carrito de compras por su ID.
     *
     * @param id ID del carrito a eliminar
     * @return ResponseEntity sin contenido (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarrito(@PathVariable Integer id) {
        carritoService.deleteCarrito(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // UPDATE TOTAL CARRITO
    // ============================================================
    /**
     * Actualiza solo el total de un carrito existente.
     *
     * @param id    ID del carrito
     * @param total nuevo valor del total
     * @return ResponseEntity con el carrito actualizado
     */
    @PutMapping("/{id}/total/{total}")
    public ResponseEntity<CarritoCompras> updateTotal(@PathVariable Integer id, @PathVariable Double total) {
        CarritoCompras actualizado = carritoService.updateTotal(id, total);
        return ResponseEntity.ok(actualizado);
    }
}
