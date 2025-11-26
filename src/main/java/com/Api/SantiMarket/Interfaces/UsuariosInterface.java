package com.Api.SantiMarket.Interfaces;

import com.Api.SantiMarket.Entities.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosInterface extends JpaRepository<Usuarios, Integer> {
    Optional<Usuarios> findByEmail(String email);

}
