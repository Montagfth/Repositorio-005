package com.cursoIntegradorI.proyectoFinal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "requerimientos")
@NoArgsConstructor
@AllArgsConstructor
public class Requerimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRequerimiento;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    private LocalDate fechaSolicitud;

    private Boolean validado;

    private String prioridad;  // ✅ AGREGAR: "Alta", "Media", "Baja"

    @ManyToOne
    @JoinColumn(name = "idProyecto")
    private Proyecto proyecto;
}

