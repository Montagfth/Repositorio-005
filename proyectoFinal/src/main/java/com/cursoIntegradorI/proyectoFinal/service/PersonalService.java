package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Asignacion;
import com.cursoIntegradorI.proyectoFinal.model.Personal;
import com.cursoIntegradorI.proyectoFinal.repository.AsignacionRepository;
import com.cursoIntegradorI.proyectoFinal.repository.PersonalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PersonalService {

    private final AsignacionRepository asignacionRepository;
    private final PersonalRepository personalRepository;

    @Transactional(readOnly = true)
    public List<Personal> listarTodos() {
        return personalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Personal> buscarPorId(Integer id) {
        return personalRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Personal> buscarPorCorreo(String correo) {
        return personalRepository.findByCorreo(correo);
    }

    @Transactional(readOnly = true)
    public List<Personal> buscarPorCargo(String cargo) {
        return personalRepository.findByCargo(cargo);
    }

    @Transactional(readOnly = true)
    public List<Personal> buscarPorTelefono(String telefono) {
        return personalRepository.findByCargo(telefono);
    }

    @Transactional
    public Personal guardar(Personal personal) {
        // Validación de correo duplicado
        personalRepository.findByCorreo(personal.getCorreo())
                .ifPresent(p -> {
                    if (!p.getIdPersonal().equals(personal.getIdPersonal())) {
                        throw new IllegalArgumentException("El correo ya está registrado");
                    }
                });
        return personalRepository.save(personal);
    }

    @Transactional
    public void eliminar(Integer id) {
        personalRepository.deleteById(id);
    }

    // Clase interna (DTO) para enviar el reporte al controlador
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class ReporteSobrecarga {
        private Personal personal;
        private double horasComprometidas; // En este mes
        private int proyectosActivos;
    }

    public List<ReporteSobrecarga> detectarSobrecarga(LocalDate inicioMes, LocalDate finMes) {
        List<Asignacion> asignacionesDelMes = asignacionRepository.findAsignacionesActivasEnRango(inicioMes, finMes);
        Map<Integer, Double> horasPorPersona = new HashMap<>();
        Map<Integer, Set<Integer>> proyectosPorPersona = new HashMap<>();

        for (Asignacion asig : asignacionesDelMes) {
            if (asig.getFechaInicio() == null || asig.getFechaFin() == null || asig.getHorasTrabajadas() == null) continue;

            // 1. Calcular duración total de la asignación en días
            long diasTotalesAsignacion = ChronoUnit.DAYS.between(asig.getFechaInicio(), asig.getFechaFin()) + 1;
            if (diasTotalesAsignacion <= 0) diasTotalesAsignacion = 1;

            // 2. Calcular horas por día (intensidad diaria)
            double horasDiarias = asig.getHorasTrabajadas() / diasTotalesAsignacion;

            // 3. Calcular cuántos días de esa asignación caen EN ESTE MES
            LocalDate inicioComputo = asig.getFechaInicio().isBefore(inicioMes) ? inicioMes : asig.getFechaInicio();
            LocalDate finComputo = asig.getFechaFin().isAfter(finMes) ? finMes : asig.getFechaFin();

            long diasEnEsteMes = ChronoUnit.DAYS.between(inicioComputo, finComputo) + 1;
            if (diasEnEsteMes < 0) diasEnEsteMes = 0;

            // 4. Sumar al acumulado de la persona
            double horasEnMes = horasDiarias * diasEnEsteMes;

            Integer idPersonal = asig.getPersonal().getIdPersonal();
            horasPorPersona.put(idPersonal, horasPorPersona.getOrDefault(idPersonal, 0.0) + horasEnMes);

            // Contar proyecto único
            proyectosPorPersona.computeIfAbsent(idPersonal, k -> new HashSet<>())
                    .add(asig.getProyectoServicio().getProyecto().getIdProyecto());
        }

        // 5. Filtrar solo los que superan el umbral (ej. 160 horas o lo que definas)
        List<ReporteSobrecarga> sobrecargados = new ArrayList<>();
        double UMBRAL_HORAS_MENSUAL = 160.0; // Estándar típico

        for (Map.Entry<Integer, Double> entry : horasPorPersona.entrySet()) {
            if (entry.getValue() > UMBRAL_HORAS_MENSUAL) {
                Personal personal = personalRepository.findById(entry.getKey()).orElse(null);
                if (personal != null) {
                    sobrecargados.add(new ReporteSobrecarga(
                            personal,
                            Math.round(entry.getValue() * 100.0) / 100.0, // Redondear 2 decimales
                            proyectosPorPersona.get(entry.getKey()).size()
                    ));
                }
            }
        }

        return sobrecargados;
    }

}