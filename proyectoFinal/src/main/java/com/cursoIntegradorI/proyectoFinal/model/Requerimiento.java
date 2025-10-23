package com.cursoIntegradorI.proyectoFinal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "requerimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Requerimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRequerimiento;

    private String descripcion;
    private LocalDate fechaSolicitud;
    private Boolean validado;

    @ManyToOne
    @JoinColumn(name = "idProyecto")
    private Proyecto proyecto;
}

