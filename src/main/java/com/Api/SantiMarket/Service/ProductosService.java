package com.Api.SantiMarket.Service;

import com.Api.SantiMarket.Entities.Productos;
import com.Api.SantiMarket.Exceptions.BadRequestException;
import com.Api.SantiMarket.Exceptions.ResourceNotFoundException;
import com.Api.SantiMarket.Interfaces.ProductosInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductosService {

    private final ProductosInterface productosRepository;

    public ProductosService(ProductosInterface productosRepository) {
        this.productosRepository = productosRepository;
    }

    // ============================================================
    // GET ALL PRODUCTOS
    // ============================================================
    /**
     * Obtiene la lista completa de productos registrados.
     *
     * @return lista de productos
     */
    public List<Productos> getAllProductos() {
        return productosRepository.findAll();
    }

    // ============================================================
    // GET PRODUCTO BY ID
    // ============================================================
    /**
     * Obtiene un producto por su ID.
     *
     * @param id identificador del producto
     * @return producto encontrado
     * @throws ResourceNotFoundException si no existe el producto
     */
    public Productos getProductosById(Integer id) {
        return productosRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con código: " + id));
    }

    // ============================================================
    // SAVE PRODUCTO
    // ============================================================
    /**
     * Crea un nuevo producto aplicando validaciones de campos obligatorios,
     * precio positivo, stock no negativo y asignación automática de fecha de creación.
     *
     * @param productos datos del producto nuevo
     * @return producto guardado
     * @throws BadRequestException si falta un campo obligatorio o datos inválidos
     */
    public Productos saveProductos(Productos productos) {
        if (productos.getDescripcion() == null || productos.getDescripcion().trim().isEmpty()) {
            throw new BadRequestException("La descripción es obligatoria.");
        }
        if (productos.getPrecio() <= 0) {
            throw new BadRequestException("El precio debe ser mayor a 0.");
        }
        if (productos.getStock() < 0) {
            throw new BadRequestException("El stock no puede ser negativo.");
        }
        if (productos.getFechaCreacion() == null) {
            productos.setFechaCreacion(LocalDateTime.now());
        }
        return productosRepository.save(productos);
    }

    // ============================================================
    // UPDATE PRODUCTO
    // ============================================================
    /**
     * Actualiza los datos de un producto existente. Solo se modifican los campos
     * enviados por parámetro (actualización parcial).
     *
     * @param id          ID del producto a actualizar
     * @param nuevosDatos datos nuevos aplicables
     * @return producto actualizado
     * @throws ResourceNotFoundException si el producto no existe
     * @throws BadRequestException       si el nombre es inválido
     */
    public Productos updateProductos(Integer id, Productos nuevosDatos) {
        Productos existente = productosRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con código: " + id));
        if (nuevosDatos.getNombre() == null || nuevosDatos.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre no puede ser nulo o blank.");
        }
        existente.setDescripcion(nuevosDatos.getDescripcion());
        existente.setPrecio(nuevosDatos.getPrecio());
        existente.setImagen(nuevosDatos.getImagen());
        existente.setCategoria(nuevosDatos.getCategoria());
        existente.setStock(nuevosDatos.getStock());
        existente.setNombre(nuevosDatos.getNombre());
        existente.setCodigo(nuevosDatos.getCodigo());

        return productosRepository.save(existente);
    }

    // ============================================================
    // ACTUALIZAR STOCK
    // ============================================================
    /**
     * Actualiza solo el stock de un producto existente.
     *
     * @param id    ID del producto
     * @param stock nuevo valor del stock
     * @return producto actualizado
     * @throws ResourceNotFoundException si el producto no existe
     */
    public Productos actualizarStock(Integer id, Integer stock) {
        Productos existente = productosRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        existente.setStock(stock);
        return productosRepository.save(existente);
    }

    // ============================================================
    // DELETE PRODUCTO BY ID
    // ============================================================
    /**
     * Elimina un producto por su ID.
     *
     * @param id identificador del producto
     * @throws ResourceNotFoundException si el producto no existe
     */
    public void deleteProductosById(Integer id) {
        if (!productosRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. Producto no encontrado con código: " + id);
        }
        productosRepository.deleteById(id);
    }
}

