package com.Api.SantiMarket.Service;

import com.Api.SantiMarket.Entities.ProductoTicket;

import com.Api.SantiMarket.Exceptions.ResourceNotFoundException;
import com.Api.SantiMarket.Interfaces.ProductoTicketInterface;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoTicketService {

    private final ProductoTicketInterface productoTicketRepository;


    public ProductoTicketService(
            ProductoTicketInterface productoTicketRepository
    ) {
        this.productoTicketRepository = productoTicketRepository;
    }

    // --------------------------------------------------------------------
    // CREAR REGISTRO DE PRODUCTO DENTRO DE UN TICKET
    // --------------------------------------------------------------------
    // Este método guarda un nuevo producto asociado a un ticket de venta.
    // Se asume que el objeto viene validado desde el controlador.
    public ProductoTicket create(ProductoTicket productoTicket) {
        return productoTicketRepository.save(productoTicket);
    }

    // --------------------------------------------------------------------
    // OBTENER TODOS LOS PRODUCTOS-TICKET
    // --------------------------------------------------------------------
    // Devuelve una lista completa de los registros en la tabla intermedia.
    public List<ProductoTicket> getAll() {
        return productoTicketRepository.findAll();
    }

    // --------------------------------------------------------------------
    // OBTENER PRODUCTO-TICKET POR ID
    // --------------------------------------------------------------------
    // Busca un registro por ID y arroja una excepción si no existe.
    public ProductoTicket getById(Integer id) {
        return productoTicketRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ProductoTicket no encontrado con ID: " + id));
    }

    // --------------------------------------------------------------------
    // ACTUALIZAR DATOS DEL PRODUCTO EN EL TICKET
    // --------------------------------------------------------------------
    // Permite cambiar la cantidad, el producto asociado o la venta asociada.
    // Siempre verifica que el registro exista antes de modificarlo.
    public ProductoTicket update(Integer id, ProductoTicket data) {

        ProductoTicket existente = getById(id);

        existente.setCantidad(data.getCantidad());
        existente.setProducto(data.getProducto());
        existente.setVenta(data.getVenta());

        return productoTicketRepository.save(existente);
    }

    // --------------------------------------------------------------------
    // ELIMINAR REGISTRO
    // --------------------------------------------------------------------
    // Antes de borrar, valida que el ID exista para evitar operaciones vacías.
    public void delete(Integer id) {

        if (!productoTicketRepository.existsById(id)) {
            throw new ResourceNotFoundException("ProductoTicket no encontrado con ID: " + id);
        }

        productoTicketRepository.deleteById(id);
    }

    // --------------------------------------------------------------------
    // LISTAR PRODUCTOS DE UN TICKET (VENTA)
    // --------------------------------------------------------------------
    // Permite filtrar todos los productos pertenecientes a una venta específica.
    public List<ProductoTicket> findByVentaId(Integer ventaId) {
        return productoTicketRepository.findByVenta_Id(ventaId);
    }
}