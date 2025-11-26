package com.Api.SantiMarket.Service;

import com.Api.SantiMarket.Entities.ProductoCarrito;
import com.Api.SantiMarket.Entities.Productos;
import com.Api.SantiMarket.Exceptions.BadRequestException;
import com.Api.SantiMarket.Exceptions.ResourceNotFoundException;
import com.Api.SantiMarket.Interfaces.ProductoCarritoInterface;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductosCarritoService {

    private final ProductoCarritoInterface productoCarritoRepository;
    private final ProductosService productosService;

    public ProductosCarritoService(ProductoCarritoInterface productoCarritoRepository,
                                   ProductosService productosService) {
        this.productoCarritoRepository = productoCarritoRepository;
        this.productosService = productosService;
    }

    // ============================================================
    // GET ALL PRODUCTOS CARRITO
    // ============================================================
    /**
     * Obtiene la lista completa de productos en carritos registrados.
     *
     * @return lista de productos en carritos
     */
    public List<ProductoCarrito> getAllProductoCarrito() {
        return productoCarritoRepository.findAll();
    }

    // ============================================================
    // GET PRODUCTO CARRITO BY ID
    // ============================================================
    /**
     * Obtiene un producto en carrito por su ID.
     *
     * @param id identificador del producto en carrito
     * @return producto en carrito encontrado
     * @throws ResourceNotFoundException si no existe el producto en carrito
     */
    public ProductoCarrito getProductoCarritoById(Integer id) {
        return productoCarritoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ProductoCarrito no encontrado con ID: " + id));
    }

    // ============================================================
    // SAVE PRODUCTO CARRITO
    // ============================================================
    /**
     * Guarda un nuevo producto en carrito o actualiza la cantidad si ya existe.
     * Aplica validaciones de stock y cantidad.
     *
     * @param nuevo datos del producto en carrito nuevo
     * @return producto en carrito guardado
     * @throws BadRequestException si el stock es insuficiente o datos inválidos
     */
    public ProductoCarrito saveProductoCarrito(ProductoCarrito nuevo) {

        Integer carritoId = nuevo.getCarrito().getId();
        Integer productoId = nuevo.getProducto().getId();

        // Buscar si ya existe ese producto en ese carrito
        var existente = productoCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId);

        // Obtener el producto real para validar stock
        Productos producto = productosService.getProductosById(productoId);
        int cantidadSolicitada = nuevo.getCantidad();

        if (existente.isPresent()) {
            // Si existe → sumamos la cantidad
            ProductoCarrito pc = existente.get();
            int nuevaCantidad = pc.getCantidad() + cantidadSolicitada;

            // Validar stock
            validarStock(producto, nuevaCantidad);

            pc.setCantidad(nuevaCantidad);
            return productoCarritoRepository.save(pc);
        }

        // Si no existe → validar stock antes de crear
        validarStock(producto, cantidadSolicitada);

        return productoCarritoRepository.save(nuevo);
    }

    // ============================================================
    // UPDATE PRODUCTO CARRITO
    // ============================================================
    /**
     * Actualiza los datos de un producto en carrito existente.
     * Recalcula el total del carrito basado en cambios de cantidad.
     *
     * @param id          ID del producto en carrito a actualizar
     * @param nuevosDatos datos nuevos aplicables
     * @return producto en carrito actualizado
     * @throws ResourceNotFoundException si el producto en carrito no existe
     * @throws BadRequestException       si los datos son inválidos o stock insuficiente
     */
    public ProductoCarrito updateProductoCarrito(Integer id, ProductoCarrito nuevosDatos) {

        ProductoCarrito existente = productoCarritoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ProductoCarrito no encontrado con ID: " + id));

        validarProductoCarrito(nuevosDatos);

        int nuevaCantidad = nuevosDatos.getCantidad();
        Productos producto = productosService.getProductosById(nuevosDatos.getProducto().getId());
        validarStock(producto, nuevaCantidad);

        // Restar el subtotal anterior
        int cantidadVieja = existente.getCantidad();
        double subtotalViejo = producto.getPrecio() * cantidadVieja;
        double totalActual = existente.getCarrito().getTotal();

        // Sumar el subtotal nuevo
        double subtotalNuevo = producto.getPrecio() * nuevaCantidad;
        existente.getCarrito().setTotal(totalActual - subtotalViejo + subtotalNuevo);

        // Actualizar los campos
        existente.setCantidad(nuevaCantidad);
        existente.setProducto(nuevosDatos.getProducto());
        existente.setCarrito(nuevosDatos.getCarrito());

        return productoCarritoRepository.save(existente);
    }

    // ============================================================
    // DELETE PRODUCTO CARRITO BY ID
    // ============================================================
    /**
     * Elimina un producto en carrito por su ID.
     * Recalcula el total del carrito restando el subtotal.
     *
     * @param id identificador del producto en carrito
     * @throws ResourceNotFoundException si el producto en carrito no existe
     */
    public void deleteProductoCarritoById(Integer id) {

        ProductoCarrito existente = productoCarritoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No existe ProductoCarrito con ID: " + id));

        // Restar subtotal del carrito antes de eliminarlo
        Productos producto = productosService.getProductosById(existente.getProducto().getId());
        int cantidad = existente.getCantidad();
        double subtotal = producto.getPrecio() * cantidad;

        double totalActual = existente.getCarrito().getTotal();
        existente.getCarrito().setTotal(totalActual - subtotal);

        productoCarritoRepository.deleteById(id);
    }

    // ============================================================
    // GET PRODUCTOS CARRITO BY CARRITO ID
    // ============================================================
    /**
     * Obtiene la lista de productos en un carrito específico por ID del carrito.
     *
     * @param carritoId identificador del carrito
     * @return lista de productos en el carrito
     * @throws ResourceNotFoundException si no se encontraron productos para el carrito
     */
    public List<ProductoCarrito> getProductosCarritoByCarritoId(Integer carritoId) {
        List<ProductoCarrito> productos = productoCarritoRepository.findByCarrito_Id(carritoId);

        if (productos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron productos para el carrito con ID: " + carritoId);
        }

        return productos;
    }

    // ============================================================
    // VACIAR CARRITO
    // ============================================================
    /**
     * Vacía completamente un carrito eliminando todos sus productos y reseteando el total.
     *
     * @param carritoId identificador del carrito
     * @throws ResourceNotFoundException si el carrito no tiene productos
     */
    @Transactional
    public void vaciarCarrito(Integer carritoId) {

        List<ProductoCarrito> productos = productoCarritoRepository.findByCarrito_Id(carritoId);

        if (productos.isEmpty()) {
            throw new ResourceNotFoundException("El carrito con ID " + carritoId + " no tiene productos.");
        }

        // Resetear total del carrito
        productos.get(0).getCarrito().setTotal(0);

        productoCarritoRepository.deleteByCarrito_Id(carritoId);
    }

    // ============================================================
    // HELPERS DE VALIDACIÓN
    // ============================================================
    /**
     * Valida los datos básicos de un ProductoCarrito.
     *
     * @param pc objeto ProductoCarrito a validar
     * @throws BadRequestException si los datos son inválidos
     */
    private void validarProductoCarrito(ProductoCarrito pc) {
        if (pc.getCantidad() == null || pc.getCantidad() <= 0) {
            throw new BadRequestException("La cantidad debe ser mayor a 0.");
        }
        if (pc.getProducto() == null) {
            throw new BadRequestException("El producto es obligatorio.");
        }
        if (pc.getCarrito() == null) {
            throw new BadRequestException("El carrito es obligatorio.");
        }
    }

    /**
     * Valida si hay suficiente stock para la cantidad solicitada.
     *
     * @param producto           objeto Producto
     * @param cantidadSolicitada cantidad requerida
     * @throws BadRequestException si el stock es insuficiente
     */
    private void validarStock(Productos producto, int cantidadSolicitada) {
        if (producto.getStock() < cantidadSolicitada) {
            throw new BadRequestException(
                    "Stock insuficiente. Stock disponible: " +
                            producto.getStock() + ", solicitado: " + cantidadSolicitada
            );
        }
    }
}
