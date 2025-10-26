package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.repository.ProyectoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    public List<Proyecto> listarTodos() {
        return proyectoRepository.findAll();
    }
}
