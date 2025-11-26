package com.Api.SantiMarket.Interfaces;

import com.Api.SantiMarket.Entities.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductosInterface extends JpaRepository<Productos, Integer> {
}
