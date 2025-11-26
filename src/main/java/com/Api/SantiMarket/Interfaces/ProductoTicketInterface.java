package com.Api.SantiMarket.Interfaces;

import com.Api.SantiMarket.Entities.ProductoTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoTicketInterface extends JpaRepository<ProductoTicket, Integer> {
    List<ProductoTicket> findByVenta_Id(Integer ventaId);
}
