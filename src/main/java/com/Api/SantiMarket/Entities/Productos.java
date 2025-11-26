package com.Api.SantiMarket.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Productos")
@Data
@EqualsAndHashCode

public class Productos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private String imagen;

    private LocalDateTime  fechaCreacion;

    private String categoria;

    private int stock;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProductoCarrito> productoCarrito;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProductoTicket> productoTicket;


}
