package com.cursoIntegradorI.proyectoFinal.repository;

import com.cursoIntegradorI.proyectoFinal.model.Informe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InformeRepository extends JpaRepository<Informe, Integer> {
    List<Informe> findByProyecto_IdProyecto(Integer idProyecto);
}