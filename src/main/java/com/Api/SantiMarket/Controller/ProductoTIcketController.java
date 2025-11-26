package com.Api.SantiMarket.Controller;

import com.Api.SantiMarket.Entities.ProductoTicket;
import com.Api.SantiMarket.Service.ProductoTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiMarket/producto-ticket")
@RequiredArgsConstructor
public class ProductoTIcketController {

    private final ProductoTicketService productoTicketService;

    // ============================================================
    // CREATE
    // ============================================================
    /**
     * Crea un nuevo registro de ProductoTicket.
     * Asocia un producto a una venta específica.
     *
     * @param productoTicket datos del registro a crear
     * @return registro creado
     */
    @PostMapping
    public ProductoTicket create(@RequestBody ProductoTicket productoTicket) {
        return productoTicketService.create(productoTicket);
    }

    // ============================================================
    // GET ALL
    // ============================================================
    /**
     * Obtiene la lista completa de registros ProductoTicket.
     *
     * @return lista de ProductoTicket
     */
    @GetMapping
    public List<ProductoTicket> getAll() {
        return productoTicketService.getAll();
    }

    // ============================================================
    // GET BY ID
    // ============================================================
    /**
     * Obtiene un registro ProductoTicket por su ID.
     *
     * @param id identificador del registro
     * @return registro encontrado
     */
    @GetMapping("/{id}")
    public ProductoTicket getById(@PathVariable Integer id) {
        return productoTicketService.getById(id);
    }

    // ============================================================
    // UPDATE
    // ============================================================
    /**
     * Actualiza los datos de un registro ProductoTicket existente.
     *
     * @param id   ID del registro a actualizar
     * @param data nuevos datos aplicables
     * @return registro actualizado
     */
    @PutMapping("/{id}")
    public ProductoTicket update(@PathVariable Integer id, @RequestBody ProductoTicket data) {
        return productoTicketService.update(id, data);
    }

    // ============================================================
    // DELETE
    // ============================================================
    /**
     * Elimina un registro ProductoTicket por su ID.
     *
     * @param id identificador del registro
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        productoTicketService.delete(id);
    }

    // ============================================================
    // GET BY VENTA_ID
    // ============================================================
    /**
     * Obtiene todos los productos asociados a una venta específica.
     *
     * @param ventaId ID de la venta
     * @return lista de ProductoTicket para esa venta
     */
    @GetMapping("/venta/{ventaId}")
    public List<ProductoTicket> findByVentaId(@PathVariable Integer ventaId) {
        return productoTicketService.findByVentaId(ventaId);
    }
}
