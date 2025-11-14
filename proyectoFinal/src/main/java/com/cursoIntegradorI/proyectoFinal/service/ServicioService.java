package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.model.Servicio;
import com.cursoIntegradorI.proyectoFinal.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    @Transactional(readOnly = true)
    public List<Servicio> listarTodos() {
        return servicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Servicio> buscarPorId(Integer id) {
        return servicioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Servicio> buscarPorProyecto(Integer idProyecto) {
        return servicioRepository.findByProyecto_IdProyecto(idProyecto);
    }

    @Transactional(readOnly = true)
    public List<Servicio> buscarPorTipo(String tipoServicio) {
        return servicioRepository.findByTipoServicio(tipoServicio);
    }

    @Transactional
    public Servicio guardar(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    @Transactional
    public void eliminar(Integer id) {
        servicioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Servicio> listarNoAsignados(Integer idProyecto) {
        return servicioRepository.findByProyecto_IdProyectoIsNull();
    }


    @Transactional
    public void asignarServicioAProyecto(Integer idProyecto, Integer idServicio) {

        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        // Crear un proyecto con solo el ID (no carga toda la entidad)
        Proyecto proyecto = new Proyecto();
        proyecto.setIdProyecto(idProyecto);

        servicio.setProyecto(proyecto);

        servicioRepository.save(servicio);
    }




}