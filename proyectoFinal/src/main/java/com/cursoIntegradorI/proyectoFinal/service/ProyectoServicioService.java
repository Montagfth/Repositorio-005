package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.ProyectoServicio;
import com.cursoIntegradorI.proyectoFinal.model.Servicio;
import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.repository.ProyectoServicioRepository;
import com.cursoIntegradorI.proyectoFinal.repository.ServicioRepository;
import com.cursoIntegradorI.proyectoFinal.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProyectoServicioService {

    private final ProyectoServicioRepository proyectoServicioRepository;
    private final ServicioRepository servicioRepository;
    private final ProyectoRepository proyectoRepository;

    /**
     * Lista todos los servicios asignados a un proyecto específico
     */
    @Transactional(readOnly = true)
    public List<ProyectoServicio> listarPorProyecto(Integer idProyecto) {
        return proyectoServicioRepository.findByProyecto_IdProyecto(idProyecto);
    }

    /**
     * Busca un ProyectoServicio específico por ID
     */
    @Transactional(readOnly = true)
    public Optional<ProyectoServicio> buscarPorId(Integer id) {
        return proyectoServicioRepository.findById(id);
    }

    /**
     * Lista todos los proyectos que usan un servicio específico del catálogo
     */
    @Transactional(readOnly = true)
    public List<ProyectoServicio> listarPorServicio(Integer idServicio) {
        return proyectoServicioRepository.findByServicio_IdServicio(idServicio);
    }

    /**
     * Asigna un servicio del catálogo a un proyecto
     * Crea una entrada en la tabla intermedia ProyectoServicio
     *
     * @param idProyecto ID del proyecto
     * @param idServicio ID del servicio del catálogo
     * @param costoAcordado Costo negociado (opcional, usa costoBase si es null)
     * @param observaciones Notas específicas (opcional)
     * @return ProyectoServicio creado
     */
    @Transactional
    public ProyectoServicio asignarServicioAProyecto(Integer idProyecto,
                                                     Integer idServicio,
                                                     Double costoAcordado,
                                                     String observaciones) {
        // Validar que el proyecto existe
        Proyecto proyecto = proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + idProyecto));

        // Validar que el servicio existe
        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + idServicio));

        // Verificar que no esté ya asignado
        boolean yaAsignado = proyectoServicioRepository
                .findByProyecto_IdProyecto(idProyecto)
                .stream()
                .anyMatch(ps -> ps.getServicio().getIdServicio().equals(idServicio));

        if (yaAsignado) {
            throw new RuntimeException("Este servicio ya está asignado al proyecto");
        }

        // Crear la relación ProyectoServicio
        ProyectoServicio proyectoServicio = new ProyectoServicio();
        proyectoServicio.setProyecto(proyecto);
        proyectoServicio.setServicio(servicio);
        proyectoServicio.setCostoAcordado(costoAcordado != null ? costoAcordado : servicio.getCostoBase());
        proyectoServicio.setObservaciones(observaciones);
        proyectoServicio.setFechaAsignacion(LocalDate.now());
        proyectoServicio.setEstado("Pendiente");

        return proyectoServicioRepository.save(proyectoServicio);
    }

    /**
     * Actualiza los datos específicos de un servicio en un proyecto
     * (costo acordado, observaciones, estado)
     */
    @Transactional
    public ProyectoServicio actualizar(Integer idProyectoServicio,
                                       Double costoAcordado,
                                       String observaciones,
                                       String estado) {
        ProyectoServicio ps = proyectoServicioRepository.findById(idProyectoServicio)
                .orElseThrow(() -> new RuntimeException("ProyectoServicio no encontrado"));

        if (costoAcordado != null) {
            ps.setCostoAcordado(costoAcordado);
        }

        ps.setObservaciones(observaciones);

        if (estado != null && !estado.trim().isEmpty()) {
            ps.setEstado(estado);
        }

        return proyectoServicioRepository.save(ps);
    }

    /**
     * Desvincula un servicio de un proyecto
     * Elimina la relación ProyectoServicio (y sus asignaciones en cascada)
     */
    @Transactional
    public void eliminar(Integer idProyectoServicio) {
        if (!proyectoServicioRepository.existsById(idProyectoServicio)) {
            throw new RuntimeException("ProyectoServicio no encontrado con ID: " + idProyectoServicio);
        }
        proyectoServicioRepository.deleteById(idProyectoServicio);
    }

    /**
     * Calcula el costo total de todos los servicios asignados a un proyecto
     */
    @Transactional(readOnly = true)
    public Double calcularCostoTotalProyecto(Integer idProyecto) {
        List<ProyectoServicio> servicios = proyectoServicioRepository.findByProyecto_IdProyecto(idProyecto);

        return servicios.stream()
                .map(ProyectoServicio::getCostoAcordado)
                .filter(costo -> costo != null)
                .reduce(0.0, Double::sum);
    }

    /**
     * Cambia el estado de un servicio en el proyecto
     */
    @Transactional
    public void cambiarEstado(Integer idProyectoServicio, String nuevoEstado) {
        ProyectoServicio ps = proyectoServicioRepository.findById(idProyectoServicio)
                .orElseThrow(() -> new RuntimeException("ProyectoServicio no encontrado"));

        ps.setEstado(nuevoEstado);
        proyectoServicioRepository.save(ps);
    }
}