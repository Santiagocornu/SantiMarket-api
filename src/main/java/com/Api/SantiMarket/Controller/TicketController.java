package com.Api.SantiMarket.Controller;

import com.Api.SantiMarket.Entities.ProductoTicket;
import com.Api.SantiMarket.Entities.Venta;
import com.Api.SantiMarket.Service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiMarket/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ventaService;

    // ============================================================
    // GET ALL
    // ============================================================
    /**
     * Obtiene la lista completa de tickets/ventas registradas.
     *
     * @return lista de ventas
     */
    @GetMapping
    public List<Venta> getAll() {
        return ventaService.getAll();
    }

    // ============================================================
    // GET BY ID
    // ============================================================
    /**
     * Obtiene una venta según su ID.
     *
     * @param id identificador de la venta
     * @return venta encontrada
     */
    @GetMapping("/{id}")
    public Venta getById(@PathVariable Integer id) {
        return ventaService.getById(id);
    }

    // ============================================================
    // CREATE
    // ============================================================
    /**
     * Crea una nueva venta. Este método permite generar un ticket
     * manualmente, aunque en la mayoría de los casos la venta se crea
     * automáticamente al procesar un carrito.
     *
     * @param venta datos iniciales de la venta
     * @return venta creada
     */
    @PostMapping
    public Venta create(@RequestBody Venta venta) {
        return ventaService.create(venta);
    }

    // ============================================================
    // UPDATE
    // ============================================================
    /**
     * Actualiza los datos de una venta existente.
     *
     * @param id        ID de la venta
     * @param ventaData campos a modificar
     * @return venta actualizada
     */
    @PutMapping("/{id}")
    public Venta update(@PathVariable Integer id, @RequestBody Venta ventaData) {
        return ventaService.update(id, ventaData);
    }

    // ============================================================
    // DELETE
    // ============================================================
    /**
     * Elimina una venta por ID.
     *
     * @param id identificador de la venta
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        ventaService.delete(id);
    }

    // ============================================================
    // AGREGAR PRODUCTO AL TICKET
    // ============================================================
    /**
     * Agrega un producto a un ticket ya existente.
     * También descuenta el stock y recalcula el total de la venta.
     *
     * @param ventaId    ID de la venta
     * @param productoId ID del producto a agregar
     * @param cantidad   cantidad solicitada
     * @return relación producto-ticket creada
     */
    @PostMapping("/{ventaId}/agregar-producto")
    public ProductoTicket agregarProducto(
            @PathVariable Integer ventaId,
            @RequestParam Integer productoId,
            @RequestParam int cantidad
    ) {
        return ventaService.agregarProducto(ventaId, productoId, cantidad);
    }

    // ============================================================
    // ACTUALIZAR TOTAL MANUALMENTE
    // ============================================================
    /**
     * Actualiza el total de una venta en base a una cantidad y
     * un precio unitario. Es útil para operaciones especiales o ajustes.
     *
     * @param ventaId        ID de la venta
     * @param cantidad       cantidad utilizada para el cálculo
     * @param precioUnitario precio por unidad
     * @return venta actualizada
     */
    @PutMapping("/{ventaId}/actualizar-total")
    public Venta actualizarTotal(
            @PathVariable Integer ventaId,
            @RequestParam int cantidad,
            @RequestParam double precioUnitario
    ) {
        return ventaService.updateTotal(ventaId, cantidad, precioUnitario);
    }

    // ============================================================
    // REALIZAR PAGO
    // ============================================================
    /**
     * Procesa el pago de un carrito completo.
     * El flujo incluye:
     * - Verificar stock de todos los productos del carrito
     * - Crear la venta asociada al usuario
     * - Descontar stock
     * - Registrar productos en la tabla ProductoTicket
     * - Vaciar el carrito
     *
     * @param carritoId ID del carrito a procesar
     * @param usuarioId ID del usuario que realiza la compra
     * @return venta generada
     */
    @PostMapping("/pago")
    public Venta realizarPago(
            @RequestParam Integer carritoId,
            @RequestParam Integer usuarioId
    ) {
        return ventaService.realizarPago(carritoId, usuarioId);
    }
}
