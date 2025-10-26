package com.cursoIntegradorI.proyectoFinal.repository;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
}
