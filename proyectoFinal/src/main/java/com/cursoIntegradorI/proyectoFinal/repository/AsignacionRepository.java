package com.cursoIntegradorI.proyectoFinal.repository;

import com.cursoIntegradorI.proyectoFinal.model.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Integer> {
    List<Asignacion> findByPersonal_IdPersonal(Integer idPersonal);
    List<Asignacion> findByProyectoServicio_IdProyectoServicio(Integer idProyectoServicio);

    // Busca asignaciones que se solapen con el rango de fechas (el mes actual)
    @Query("SELECT a FROM Asignacion a WHERE " +
            "(a.fechaInicio <= :finMes AND a.fechaFin >= :inicioMes) " +
            "AND a.personal IS NOT NULL")
    List<Asignacion> findAsignacionesActivasEnRango(
            @Param("inicioMes") LocalDate inicioMes,
            @Param("finMes") LocalDate finMes);
}