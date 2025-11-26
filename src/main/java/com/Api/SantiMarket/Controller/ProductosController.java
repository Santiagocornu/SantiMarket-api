package com.Api.SantiMarket.Controller;

import com.Api.SantiMarket.Entities.Productos;
import com.Api.SantiMarket.Service.ProductosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiMarket/productos")
public class ProductosController {

    private final ProductosService productosService;

    public ProductosController(ProductosService productosService) {
        this.productosService = productosService;
    }

    // ============================================================
    // GET ALL PRODUCTOS
    // ============================================================
    /**
     * Obtiene la lista completa de productos registrados.
     *
     * @return ResponseEntity con la lista de productos
     */
    @GetMapping
    public ResponseEntity<List<Productos>> getAllProductos() {
        return ResponseEntity.ok(productosService.getAllProductos());
    }

    // ============================================================
    // GET PRODUCTO BY ID
    // ============================================================
    /**
     * Obtiene un producto por su código.
     *
     * @param codigo código del producto
     * @return ResponseEntity con el producto encontrado
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Productos> getProductosById(@PathVariable Integer codigo) {
        return ResponseEntity.ok(productosService.getProductosById(codigo));
    }

    // ============================================================
    // CREATE PRODUCTO
    // ============================================================
    /**
     * Crea un nuevo producto.
     *
     * @param productos datos del producto a crear
     * @return ResponseEntity con el producto creado
     */
    @PostMapping
    public ResponseEntity<Productos> createProductos(@RequestBody Productos productos) {
        Productos savedProducto = productosService.saveProductos(productos);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
    }

    // ============================================================
    // UPDATE PRODUCTO COMPLETO
    // ============================================================
    /**
     * Actualiza completamente un producto existente.
     *
     * @param codigo      código del producto a actualizar
     * @param nuevosDatos datos actualizados del producto
     * @return ResponseEntity con el producto actualizado
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<Productos> updateProductos(
            @PathVariable Integer codigo,
            @RequestBody Productos nuevosDatos
    ) {
        return ResponseEntity.ok(productosService.updateProductos(codigo, nuevosDatos));
    }

    // ============================================================
    // UPDATE STOCK DEL PRODUCTO
    // ============================================================
    /**
     * Actualiza únicamente el stock de un producto.
     *
     * @param codigo código del producto a actualizar
     * @param stock  nuevo valor de stock
     * @return ResponseEntity con el producto actualizado
     */
    @PutMapping("/{codigo}/stock")
    public ResponseEntity<Productos> actualizarStock(
            @PathVariable Integer codigo,
            @RequestBody Integer stock
    ) {
        return ResponseEntity.ok(productosService.actualizarStock(codigo, stock));
    }

    // ============================================================
    // DELETE PRODUCTO BY ID
    // ============================================================
    /**
     * Elimina un producto por su código.
     *
     * @param codigo código del producto a eliminar
     * @return ResponseEntity sin contenido (204)
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deleteProductosById(@PathVariable Integer codigo) {
        productosService.deleteProductosById(codigo);
        return ResponseEntity.noContent().build();
    }
}
