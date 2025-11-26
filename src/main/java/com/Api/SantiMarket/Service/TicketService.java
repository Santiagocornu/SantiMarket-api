package com.Api.SantiMarket.Service;

import com.Api.SantiMarket.Entities.ProductoCarrito;
import com.Api.SantiMarket.Entities.ProductoTicket;
import com.Api.SantiMarket.Entities.Productos;
import com.Api.SantiMarket.Entities.Usuarios;
import com.Api.SantiMarket.Entities.Venta;
import com.Api.SantiMarket.Exceptions.BadRequestException;
import com.Api.SantiMarket.Exceptions.ResourceNotFoundException;
import com.Api.SantiMarket.Interfaces.ProductoCarritoInterface;
import com.Api.SantiMarket.Interfaces.ProductoTicketInterface;
import com.Api.SantiMarket.Interfaces.ProductosInterface;
import com.Api.SantiMarket.Interfaces.VentaInterface;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final VentaInterface ventaRepository;
    private final ProductoTicketInterface productoTicketRepository;
    private final ProductosInterface productosRepository;
    private final ProductoCarritoInterface productoCarritoRepository;

    // ============================================================
    // GET ALL
    // ============================================================
    /**
     * Obtiene la lista completa de tickets/ventas.
     *
     * @return lista de ventas
     */
    public List<Venta> getAll() {
        return ventaRepository.findAll();
    }

    // ============================================================
    // GET BY ID
    // ============================================================
    /**
     * Obtiene una venta por su ID.
     *
     * @param id identificador de la venta
     * @return venta encontrada
     * @throws ResourceNotFoundException si la venta no existe
     */
    public Venta getById(Integer id) {
        return ventaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Venta no encontrada con ID: " + id));
    }

    // ============================================================
    // CREATE
    // ============================================================
    /**
     * Crea una nueva venta.
     *
     * @param venta datos iniciales de la venta
     * @return venta guardada
     */
    public Venta create(Venta venta) {

        if (venta.getFechaCreacion() == null)
            venta.setFechaCreacion(LocalDateTime.now());

        return ventaRepository.save(venta);
    }

    // ============================================================
    // UPDATE
    // ============================================================
    /**
     * Actualiza los datos de una venta existente.
     * Realiza actualización total, no parcial.
     *
     * @param id        ID de la venta a actualizar
     * @param ventaData nuevos datos aplicables
     * @return venta actualizada
     * @throws ResourceNotFoundException si la venta no existe
     */
    public Venta update(Integer id, Venta ventaData) {

        Venta venta = getById(id);

        venta.setDescripcion(ventaData.getDescripcion());
        venta.setEstado(ventaData.getEstado());
        venta.setMetodoPago(ventaData.getMetodoPago());
        venta.setCiudad(ventaData.getCiudad());
        venta.setProvincia(ventaData.getProvincia());
        venta.setPais(ventaData.getPais());
        venta.setTotal(ventaData.getTotal());

        return ventaRepository.save(venta);
    }

    // ============================================================
    // DELETE
    // ============================================================
    /**
     * Elimina una venta por su ID.
     *
     * @param id identificador de la venta
     * @throws ResourceNotFoundException si no existe
     */
    public void delete(Integer id) {

        if (!ventaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: venta no encontrada con ID: " + id);
        }

        ventaRepository.deleteById(id);
    }

    // ============================================================
    // AGREGAR PRODUCTO AL TICKET (RESTA STOCK)
    // ============================================================
    /**
     * Agrega un producto a una venta y descuenta el stock del producto correspondiente.
     *
     * @param ventaId    ID de la venta
     * @param productoId ID del producto
     * @param cantidad   cantidad solicitada
     * @return productoTicket creado
     * @throws ResourceNotFoundException si venta o producto no existen
     * @throws BadRequestException       si no hay stock suficiente
     */
    @Transactional
    public ProductoTicket agregarProducto(Integer ventaId, Integer productoId, int cantidad) {

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Venta no encontrada con ID: " + ventaId));

        Productos producto = productosRepository.findById(productoId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con ID: " + productoId));

        if (cantidad <= 0) {
            throw new BadRequestException("La cantidad debe ser mayor a 0.");
        }

        if (producto.getStock() < cantidad) {
            throw new BadRequestException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        // Restar stock
        producto.setStock(producto.getStock() - cantidad);
        productosRepository.save(producto);

        // Crear ProductoTicket
        ProductoTicket pt = new ProductoTicket();
        pt.setProducto(producto);
        pt.setVenta(venta);
        pt.setCantidad(cantidad);

        productoTicketRepository.save(pt);

        // Actualizar total
        double subtotal = producto.getPrecio() * cantidad;
        venta.setTotal((venta.getTotal() == null ? 0 : venta.getTotal()) + subtotal);
        ventaRepository.save(venta);

        return pt;
    }

    // ============================================================
    // REALIZAR PAGO (PROCESA CARRITO)
    // ============================================================
    /**
     * Procesa el pago completo de un carrito:
     * - Verifica stock de todos los productos
     * - Crea la venta
     * - Descarga stock
     * - Crea ProductoTicket por cada item
     * - Vacia el carrito
     *
     * @param carritoId ID del carrito
     * @param usuarioId ID del usuario que paga
     * @return venta procesada
     * @throws BadRequestException       si el carrito está vacío o falta stock
     * @throws ResourceNotFoundException si el usuario o productos no existen
     */
    @Transactional
    public Venta realizarPago(Integer carritoId, Integer usuarioId) {

        List<ProductoCarrito> items = productoCarritoRepository.findByCarrito_Id(carritoId);

        if (items.isEmpty()) {
            throw new BadRequestException("El carrito está vacío.");
        }

        // Verificar stock y total preliminar
        double total = 0;

        for (ProductoCarrito pc : items) {
            Productos p = pc.getProducto();

            if (p.getStock() < pc.getCantidad()) {
                throw new BadRequestException(
                        "Stock insuficiente para el producto: " + p.getNombre()
                );
            }

            total += p.getPrecio() * pc.getCantidad();
        }

        // Crear venta
        Venta venta = new Venta();
        venta.setEstado("Pendiente");
        venta.setFechaCreacion(LocalDateTime.now());
        venta.setTotal(total);

        Usuarios u = new Usuarios();
        u.setId(usuarioId);
        venta.setUsuario(u);

        venta = ventaRepository.save(venta);

        // Procesar items
        for (ProductoCarrito pc : items) {

            Productos p = pc.getProducto();
            int cantidad = pc.getCantidad();

            p.setStock(p.getStock() - cantidad);
            productosRepository.save(p);

            ProductoTicket pt = new ProductoTicket();
            pt.setProducto(p);
            pt.setVenta(venta);
            pt.setCantidad(cantidad);

            productoTicketRepository.save(pt);
        }

        // Vaciar carrito
        productoCarritoRepository.deleteByCarrito_Id(carritoId);

        return venta;
    }

    // ------------------------------------------------------------
// ACTUALIZAR TOTAL DEL TICKET
// ------------------------------------------------------------
    public Venta updateTotal(Integer ventaId, int cantidad, double precioUnitario) {

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + ventaId));

        double nuevoTotal = cantidad * precioUnitario;
        venta.setTotal(nuevoTotal);

        return ventaRepository.save(venta);
    }


}
