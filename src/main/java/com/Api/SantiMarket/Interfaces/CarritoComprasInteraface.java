package com.Api.SantiMarket.Interfaces;

import com.Api.SantiMarket.Entities.CarritoCompras;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoComprasInteraface extends JpaRepository<CarritoCompras, Integer> {
    CarritoCompras findByUsuarioId(Integer usuarioId);
}
