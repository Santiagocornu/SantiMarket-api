package com.Api.SantiMarket.Interfaces;

import com.Api.SantiMarket.Entities.ProductoCarrito;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoCarritoInterface extends JpaRepository<ProductoCarrito, Integer> {

    List<ProductoCarrito> findByCarrito_Id(Integer carritoId);

    void deleteByCarrito_Id(Integer carritoId);

    Optional<ProductoCarrito> findByCarritoIdAndProductoId(Integer carritoId, Integer productoId);


}
