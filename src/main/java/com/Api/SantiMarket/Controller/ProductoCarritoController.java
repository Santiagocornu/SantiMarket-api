package com.Api.SantiMarket.Controller;

import com.Api.SantiMarket.Entities.ProductoCarrito;
import com.Api.SantiMarket.Service.ProductosCarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiMarket/productoCarrito")
public class ProductoCarritoController {

    private final ProductosCarritoService productosCarritoService;

    public ProductoCarritoController(ProductosCarritoService productosCarritoService) {
        this.productosCarritoService = productosCarritoService;
    }

    // ============================================================
    // GET ALL PRODUCTOS CARRITO
    // ============================================================
    /**
     * Obtiene la lista completa de productos en carritos.
     *
     * @return ResponseEntity con lista de productos en carritos
     */
    @GetMapping
    public ResponseEntity<List<ProductoCarrito>> getAll() {
        return ResponseEntity.ok(productosCarritoService.getAllProductoCarrito());
    }

    // ============================================================
    // GET PRODUCTO CARRITO BY ID
    // ============================================================
    /**
     * Obtiene un producto en carrito por su ID.
     *
     * @param id ID del producto en carrito
     * @return ResponseEntity con el producto en carrito encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoCarrito> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productosCarritoService.getProductoCarritoById(id));
    }

    // ============================================================
    // CREATE PRODUCTO CARRITO
    // ============================================================
    /**
     * Crea un nuevo producto en carrito o actualiza la cantidad si ya existe.
     *
     * @param productoCarrito datos del producto en carrito a crear
     * @return ResponseEntity con el producto en carrito creado
     */
    @PostMapping
    public ResponseEntity<ProductoCarrito> create(@RequestBody ProductoCarrito productoCarrito) {
        return ResponseEntity.ok(productosCarritoService.saveProductoCarrito(productoCarrito));
    }

    // ============================================================
    // UPDATE PRODUCTO CARRITO
    // ============================================================
    /**
     * Actualiza los datos de un producto en carrito existente.
     *
     * @param id          ID del producto en carrito a actualizar
     * @param nuevosDatos datos nuevos del producto en carrito
     * @return ResponseEntity con el producto en carrito actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductoCarrito> update(
            @PathVariable Integer id,
            @RequestBody ProductoCarrito nuevosDatos
    ) {
        return ResponseEntity.ok(productosCarritoService.updateProductoCarrito(id, nuevosDatos));
    }

    // ============================================================
    // DELETE PRODUCTO CARRITO BY ID
    // ============================================================
    /**
     * Elimina un producto en carrito por su ID.
     *
     * @param id ID del producto en carrito a eliminar
     * @return ResponseEntity sin contenido (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productosCarritoService.deleteProductoCarritoById(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // GET PRODUCTOS CARRITO BY CARRITO ID
    // ============================================================
    /**
     * Obtiene la lista de productos en un carrito específico por ID del carrito.
     *
     * @param carritoId ID del carrito
     * @return ResponseEntity con lista de productos en el carrito
     */
    @GetMapping("/carrito/{carritoId}")
    public ResponseEntity<List<ProductoCarrito>> getByCarritoId(@PathVariable Integer carritoId) {
        return ResponseEntity.ok(productosCarritoService.getProductosCarritoByCarritoId(carritoId));
    }

    // ============================================================
    // VACIAR CARRITO
    // ============================================================
    /**
     * Vacía completamente un carrito eliminando todos sus productos.
     *
     * @param carritoId ID del carrito a vaciar
     * @return ResponseEntity sin contenido (204)
     */
    @DeleteMapping("/carrito/{carritoId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Integer carritoId) {
        productosCarritoService.vaciarCarrito(carritoId);
        return ResponseEntity.noContent().build();
    }
}
