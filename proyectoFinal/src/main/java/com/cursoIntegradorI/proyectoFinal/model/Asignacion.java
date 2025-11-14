package com.cursoIntegradorI.proyectoFinal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "asignaciones")
@NoArgsConstructor
@AllArgsConstructor
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAsignacion")
    private Integer idAsignacion;

    @ManyToOne
    @JoinColumn(name = "idProyectoServicio", nullable = false)
    private ProyectoServicio proyectoServicio;  // ✅ Asigna personal a un servicio dentro de un proyecto

    @ManyToOne
    @JoinColumn(name = "idPersonal", nullable = false)
    private Personal personal;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private String rol;                // ej: "Desarrollador Frontend"

    private Double horasTrabajadas;

    private Double tarifaPorHora;      // ✅ AGREGAR: tarifa específica de esta persona en este proyecto
}
