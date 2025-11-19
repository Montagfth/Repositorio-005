package com.cursoIntegradorI.proyectoFinal.repository;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    List<Proyecto> findByCliente_IdCliente(Integer idCliente);
    List<Proyecto> findByEstado(String estado);
    List<Proyecto> findByNombreContainingIgnoreCase(String nombre);

    long countByCliente_IdCliente(Integer idCliente);
}