package com.Api.SantiMarket.Interfaces;

import com.Api.SantiMarket.Entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaInterface extends JpaRepository<Venta, Integer> {
}
