package com.cursoIntegradorI.proyectoFinal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "proyecto_servicios")
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProyectoServicio;

    @ManyToOne
    @JoinColumn(name = "idProyecto", nullable = false)
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "idServicio", nullable = false)
    private Servicio servicio;  // ✅ Referencia al catálogo

    // Datos específicos de este servicio EN ESTE PROYECTO
    private Double costoAcordado;      // Puede ser diferente al costoBase

    @Column(length = 1000)
    private String observaciones;      // Notas específicas del proyecto

    private LocalDate fechaAsignacion;

    private String estado;             // "Pendiente", "En Proceso", "Completado"

    // ✅ Relación con asignaciones de personal
    @OneToMany(mappedBy = "proyectoServicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asignacion> asignaciones;
}
