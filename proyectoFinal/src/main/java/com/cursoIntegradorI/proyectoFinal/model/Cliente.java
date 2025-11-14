package com.cursoIntegradorI.proyectoFinal.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "clientes")
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente")
    private Integer idCliente;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(unique = true, length = 11)  // ✅ AGREGAR unique
    private String ruc;

    private String direccion;
    private String telefono;

    @Column(unique = true)  // ✅ AGREGAR unique
    private String correo;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proyecto> proyectos;
}

