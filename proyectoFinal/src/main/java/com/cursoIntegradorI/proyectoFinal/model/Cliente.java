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

    @Column(unique = true, length = 11)
    private String ruc;

    private String direccion;
    private String telefono;

    @Column(unique = true)
    private String correo;

    // ✅ CAMBIAR ESTA LÍNEA - Eliminar cascade y orphanRemoval
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    @ToString.Exclude  // ✅ AGREGAR para evitar loops infinitos en logs
    @EqualsAndHashCode.Exclude  // ✅ AGREGAR para evitar problemas con equals/hashCode
    private List<Proyecto> proyectos;
}