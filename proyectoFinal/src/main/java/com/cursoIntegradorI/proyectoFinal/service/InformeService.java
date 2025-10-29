package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Informe;
import com.cursoIntegradorI.proyectoFinal.repository.InformeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InformeService {

    private final InformeRepository informeRepository;

    @Transactional(readOnly = true)
    public List<Informe> listarTodos() {
        return informeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Informe> buscarPorId(Integer id) {
        return informeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Informe> buscarPorProyecto(Integer idProyecto) {
        return informeRepository.findByProyecto_IdProyecto(idProyecto);
    }

    @Transactional
    public Informe guardar(Informe informe) {
        return informeRepository.save(informe);
    }

    @Transactional
    public void eliminar(Integer id) {
        informeRepository.deleteById(id);
    }
}