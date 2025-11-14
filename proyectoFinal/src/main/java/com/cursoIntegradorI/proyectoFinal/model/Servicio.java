package com.cursoIntegradorI.proyectoFinal.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "catalogo_servicios")  // ✅ Nombre más descriptivo
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idServicio;

    private String tipoServicio;

    @Column(length = 1000)
    private String descripcion;

    private Double costoBase;

    @OneToMany(mappedBy = "servicio")
    private List<ProyectoServicio> proyectosQueUsanEsteServicio;
}


