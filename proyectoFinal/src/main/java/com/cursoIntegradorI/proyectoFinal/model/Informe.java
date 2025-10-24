package com.cursoIntegradorI.proyectoFinal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "informes")
@NoArgsConstructor
@AllArgsConstructor
public class Informe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInforme;

    private String descripcion;
    private LocalDate fechaEntrega;

    @ManyToOne
    @JoinColumn(name = "idProyecto")
    private Proyecto proyecto;
}

