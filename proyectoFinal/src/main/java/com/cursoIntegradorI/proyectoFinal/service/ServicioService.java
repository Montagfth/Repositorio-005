package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Servicio;
import com.cursoIntegradorI.proyectoFinal.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service para gestionar el CATÁLOGO de servicios
 * (servicios base/plantilla que se pueden asignar a proyectos)
 */
@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    /**
     * Lista todos los servicios del catálogo
     */
    @Transactional(readOnly = true)
    public List<Servicio> listarCatalogo() {
        return servicioRepository.findAll();
    }

    /**
     * Busca un servicio del catálogo por ID
     */
    @Transactional(readOnly = true)
    public Optional<Servicio> buscarPorId(Integer id) {
        return servicioRepository.findById(id);
    }

    /**
     * Busca servicios del catálogo por tipo
     */
    @Transactional(readOnly = true)
    public List<Servicio> buscarPorTipo(String tipoServicio) {
        return servicioRepository.findByTipoServicio(tipoServicio);
    }

    /**
     * Guarda un servicio en el catálogo
     * (crear o actualizar)
     */
    @Transactional
    public Servicio guardarEnCatalogo(Servicio servicio) {
        // Validaciones básicas
        if (servicio.getTipoServicio() == null || servicio.getTipoServicio().trim().isEmpty()) {
            throw new RuntimeException("El tipo de servicio es obligatorio");
        }

        if (servicio.getCostoBase() == null || servicio.getCostoBase() < 0) {
            throw new RuntimeException("El costo base debe ser mayor o igual a 0");
        }

        return servicioRepository.save(servicio);
    }

    /**
     * Elimina un servicio del catálogo
     * NOTA: Solo se puede eliminar si no está siendo usado en ningún proyecto
     */
    @Transactional
    public void eliminarDelCatalogo(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));

        // Verificar si está siendo usado en proyectos
        if (servicio.getProyectosQueUsanEsteServicio() != null &&
                !servicio.getProyectosQueUsanEsteServicio().isEmpty()) {
            throw new RuntimeException(
                    "No se puede eliminar este servicio porque está siendo usado en " +
                            servicio.getProyectosQueUsanEsteServicio().size() + " proyecto(s)"
            );
        }

        servicioRepository.deleteById(id);
    }

    /**
     * Cuenta cuántos proyectos están usando un servicio del catálogo
     */
    @Transactional(readOnly = true)
    public long contarProyectosQueUsanServicio(Integer idServicio) {
        return servicioRepository.findById(idServicio)
                .map(servicio -> servicio.getProyectosQueUsanEsteServicio() != null
                        ? servicio.getProyectosQueUsanEsteServicio().size()
                        : 0)
                .orElse(0)
                .longValue();
    }

    /**
     * Actualiza el costo base de un servicio del catálogo
     * NOTA: No afecta los costos ya acordados en proyectos existentes
     */
    @Transactional
    public Servicio actualizarCostoBase(Integer idServicio, Double nuevoCostoBase) {
        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        if (nuevoCostoBase == null || nuevoCostoBase < 0) {
            throw new RuntimeException("El costo base debe ser mayor o igual a 0");
        }

        servicio.setCostoBase(nuevoCostoBase);
        return servicioRepository.save(servicio);
    }
}