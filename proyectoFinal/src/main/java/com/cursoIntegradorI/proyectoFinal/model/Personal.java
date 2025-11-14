package com.cursoIntegradorI.proyectoFinal.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "personal")
@NoArgsConstructor
@AllArgsConstructor
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPersonal;

    @Column(nullable = false, length = 200)
    private String nombre;

    private String cargo;

    @Column(unique = true)  // ✅ AGREGAR unique
    private String correo;

    private String telefono;

    private Double tarifaEstimadaPorHora;  // ✅ AGREGAR: tarifa base de este empleado

    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asignacion> asignaciones;
}


