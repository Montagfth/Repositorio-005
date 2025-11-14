package com.cursoIntegradorI.proyectoFinal.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "proyectos")
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProyecto;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(length = 2000)
    private String descripcion;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;

    private String estado;  // ✅ Considerar ENUM

    private Double presupuestoTotal;  // ✅ AGREGAR

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.PERSIST)
    private List<ProyectoServicio> servicios;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Requerimiento> requerimientos;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Informe> informes;
}