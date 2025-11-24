package com.cursoIntegradorI.proyectoFinal.repository;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    List<Proyecto> findByCliente_IdCliente(Integer idCliente);
    List<Proyecto> findByEstado(String estado);
    List<Proyecto> findByNombreContainingIgnoreCase(String nombre);

    long countByCliente_IdCliente(Integer idCliente);
    // Esta consulta busca proyectos cuya fecha fin ya pasó, que NO están completados
    // y que tampoco están ya marcados como retrasados (para no repetir trabajo).
    @Modifying
    @Transactional
    @Query("UPDATE Proyecto p SET p.estado = 'Retrasado' " +
            "WHERE p.fechaFin < :fechaHoy " +
            "AND p.estado NOT IN ('Completado', 'Retrasado')")
    void actualizarProyectosVencidos(@Param("fechaHoy") LocalDate fechaHoy);
}