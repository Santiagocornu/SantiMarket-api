package com.Api.SantiMarket.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ProductoCarrito")
@Data
@EqualsAndHashCode
public class ProductoCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Productos producto;

    @ManyToOne
    @JoinColumn(name = "carrito_id",nullable = false)
    private CarritoCompras carrito;


}
