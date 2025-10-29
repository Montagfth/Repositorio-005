package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    @Transactional(readOnly = true)
    public List<Proyecto> listarTodos() {
        return proyectoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Proyecto> buscarPorId(Integer id) {
        return proyectoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Proyecto> buscarPorCliente(Integer idCliente) {
        return proyectoRepository.findByCliente_IdCliente(idCliente);
    }

    @Transactional(readOnly = true)
    public List<Proyecto> buscarPorEstado(String estado) {
        return proyectoRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public List<Proyecto> buscarPorNombre(String nombre) {
        return proyectoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional
    public Proyecto guardar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Transactional
    public void eliminar(Integer id) {
        proyectoRepository.deleteById(id);
    }
}