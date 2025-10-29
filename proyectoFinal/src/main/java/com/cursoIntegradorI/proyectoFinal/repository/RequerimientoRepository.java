package com.cursoIntegradorI.proyectoFinal.repository;

import com.cursoIntegradorI.proyectoFinal.model.Requerimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RequerimientoRepository extends JpaRepository<Requerimiento, Integer> {
    List<Requerimiento> findByProyecto_IdProyecto(Integer idProyecto);
    List<Requerimiento> findByValidado(Boolean validado);
}