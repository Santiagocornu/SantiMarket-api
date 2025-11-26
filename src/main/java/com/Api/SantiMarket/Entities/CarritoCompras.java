package com.Api.SantiMarket.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Table(name = "CarritoCompras")
@Data
@EqualsAndHashCode
public class CarritoCompras {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private double total;
    @OneToOne
    @JoinColumn(name="usuario_id")
    private Usuarios usuario;


    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProductoCarrito> productos;


}
