package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Requerimiento;
import com.cursoIntegradorI.proyectoFinal.repository.RequerimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequerimientoService {

    private final RequerimientoRepository requerimientoRepository;

    @Transactional(readOnly = true)
    public List<Requerimiento> listarTodos() {
        return requerimientoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Requerimiento> buscarPorId(Integer id) {
        return requerimientoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Requerimiento> buscarPorProyecto(Integer idProyecto) {
        return requerimientoRepository.findByProyecto_IdProyecto(idProyecto);
    }

    @Transactional(readOnly = true)
    public List<Requerimiento> buscarPorValidado(Boolean validado) {
        return requerimientoRepository.findByValidado(validado);
    }

    @Transactional
    public Requerimiento guardar(Requerimiento requerimiento) {
        return requerimientoRepository.save(requerimiento);
    }

    @Transactional
    public void eliminar(Integer id) {
        requerimientoRepository.deleteById(id);
    }
}