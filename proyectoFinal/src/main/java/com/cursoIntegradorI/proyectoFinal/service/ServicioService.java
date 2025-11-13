package com.cursoIntegradorI.proyectoFinal.service;

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
}