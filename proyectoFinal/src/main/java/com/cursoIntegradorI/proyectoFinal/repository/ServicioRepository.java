package com.cursoIntegradorI.proyectoFinal.repository;

import com.cursoIntegradorI.proyectoFinal.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    List<Servicio> findByTipoServicio(String tipoServicio);
    // ✅ Eliminar métodos relacionados con proyectos
}