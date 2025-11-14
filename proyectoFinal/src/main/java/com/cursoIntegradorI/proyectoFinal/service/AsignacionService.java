package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Asignacion;
import com.cursoIntegradorI.proyectoFinal.repository.AsignacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;

    @Transactional(readOnly = true)
    public List<Asignacion> listarTodas() {
        return asignacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Asignacion> buscarPorId(Integer id) {
        return asignacionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Asignacion> buscarPorPersonal(Integer idPersonal) {
        return asignacionRepository.findByPersonal_IdPersonal(idPersonal);
    }

    @Transactional(readOnly = true)
    public List<Asignacion> buscarPorServicio(Integer idServicio) {
        return asignacionRepository.findByProyectoServicio_IdProyectoServicio(idServicio);
    }

    @Transactional
    public Asignacion guardar(Asignacion asignacion) {
        return asignacionRepository.save(asignacion);
    }

    @Transactional
    public void eliminar(Integer id) {
        asignacionRepository.deleteById(id);
    }
}