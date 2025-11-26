package com.Api.SantiMarket.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "ProductoTicket")
@Data
@EqualsAndHashCode
public class ProductoTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "producto_id",nullable = false)
    private Productos producto;

    @ManyToOne
    @JoinColumn(name = "venta_id",nullable = false)
    private Venta venta;
}
