package com.cursoIntegradorI.proyectoFinal.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol;  // ✅ AGREGAR: "ADMIN", "USER", "VIEWER"

    private Boolean activo;  // ✅ AGREGAR: para deshabilitar usuarios

    @ManyToOne  // ✅ CONSIDERAR: vincular usuario con personal
    @JoinColumn(name = "idPersonal")
    private Personal personal;
}




