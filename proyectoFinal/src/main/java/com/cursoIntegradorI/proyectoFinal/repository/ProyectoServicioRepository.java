package com.cursoIntegradorI.proyectoFinal.repository;

import com.cursoIntegradorI.proyectoFinal.model.ProyectoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProyectoServicioRepository extends JpaRepository<ProyectoServicio, Integer> {
    List<ProyectoServicio> findByProyecto_IdProyecto(Integer idProyecto);
    List<ProyectoServicio> findByServicio_IdServicio(Integer idServicio);
}