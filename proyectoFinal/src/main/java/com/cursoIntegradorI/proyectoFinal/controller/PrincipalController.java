package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.service.PersonalService;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class PrincipalController {

    private final ProyectoService proyectoService;
    private final PersonalService personalService;

    @GetMapping({"/", "/principal"})
    public String mostrarCalendario(@RequestParam(defaultValue = "0") int offset, Model model) {
        proyectoService.verificarYActualizarRetrasos();
        model.addAttribute("currentPage", "principal");
        YearMonth mesActual = YearMonth.now().plusMonths(offset);
        int diasEnMes = mesActual.lengthOfMonth();

        LocalDate inicioMes = mesActual.atDay(1);
        LocalDate finMes = mesActual.atEndOfMonth();

        // Generar lista de días del mes
        List<LocalDate> dias = IntStream.rangeClosed(1, diasEnMes)
                .mapToObj(mesActual::atDay)
                .collect(Collectors.toList());

        // Obtener todos los proyectos
        List<Proyecto> proyectos = proyectoService.listarTodos();

        // ✅ NUEVA LÓGICA: Detectar proyectos retrasados
        List<Proyecto> proyectosRetrasados = proyectos.stream()
                .filter(p -> p.getFechaFin() != null &&
                        p.getFechaFin().isBefore(LocalDate.now()) &&
                        !"Completado".equalsIgnoreCase(p.getEstado()))
                .collect(Collectors.toList());

        // ✅ NUEVA LÓGICA: Calcular proyectos en riesgo de presupuesto
        long proyectosEnRiesgo = proyectos.stream()
                .filter(p -> p.getPresupuestoTotal() != null && p.getPresupuestoTotal() > 0)
                .filter(p -> p.getServicios() != null && !p.getServicios().isEmpty())
                .filter(p -> {
                    double costoTotal = p.getServicios().stream()
                            .mapToDouble(ps -> ps.getCostoAcordado() != null ? ps.getCostoAcordado() : 0.0)
                            .sum();
                    return costoTotal > p.getPresupuestoTotal() * 0.9; // 90% del presupuesto
                })
                .count();

        // ✅ NUEVA LÓGICA: Calcular proyectos por estado
        long proyectosActivos = proyectos.stream()
                .filter(p -> "En Curso".equalsIgnoreCase(p.getEstado()))
                .count();

        long proyectosEnProgreso = proyectos.stream()
                .filter(p -> "En Curso".equalsIgnoreCase(p.getEstado()) ||
                        "Planificado".equalsIgnoreCase(p.getEstado()))
                .count();

        long proyectosFinalizados = proyectos.stream()
                .filter(p -> "Completado".equalsIgnoreCase(p.getEstado()))
                .count();

        // Obtener nombre del mes en español
        String nombreMes = mesActual.getMonth()
                .getDisplayName(TextStyle.FULL, Locale.of("es", "ES"));
        nombreMes = Character.toUpperCase(nombreMes.charAt(0)) + nombreMes.substring(1);

        // ✅ NUEVA LÓGICA: Crear mapa de proyectos por día para acceso rápido
        Map<LocalDate, List<Proyecto>> proyectosPorDia = new HashMap<>();
        for (Proyecto proyecto : proyectos) {
            if (proyecto.getFechaInicio() != null && proyecto.getFechaFin() != null) {
                LocalDate fecha = proyecto.getFechaInicio();
                while (!fecha.isAfter(proyecto.getFechaFin())) {
                    proyectosPorDia.computeIfAbsent(fecha, k -> new ArrayList<>()).add(proyecto);
                    fecha = fecha.plusDays(1);
                }
            }
        }
        // ✅ LÓGICA ACTUALIZADA: Obtener la LISTA de proyectos en riesgo
        List<Proyecto> listadoProyectosEnRiesgo = proyectos.stream()
                .filter(p -> p.getPresupuestoTotal() != null && p.getPresupuestoTotal() > 0)
                .filter(p -> {
                    double costoTotal = p.getServicios() != null ? p.getServicios().stream()
                            .mapToDouble(ps -> ps.getCostoAcordado() != null ? ps.getCostoAcordado() : 0.0)
                            .sum() : 0.0;
                    return costoTotal > p.getPresupuestoTotal() * 0.9; // Mayor al 90%
                })
                .collect(Collectors.toList());

        // Agregar atributos al modelo
        model.addAttribute("offset", offset);
        model.addAttribute("nombreMes", nombreMes);
        model.addAttribute("año", mesActual.getYear());
        model.addAttribute("hoy", LocalDate.now());
        model.addAttribute("dias", dias);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("proyectosPorDia", proyectosPorDia);

        // ✅ NUEVOS ATRIBUTOS PARA ALERTAS
        model.addAttribute("proyectosActivos", proyectosActivos);
        model.addAttribute("proyectosEnProgreso", proyectosEnProgreso);
        model.addAttribute("proyectosFinalizados", proyectosFinalizados);
        model.addAttribute("proyectosRetrasados", proyectosRetrasados.size());
        model.addAttribute("proyectosEnRiesgo", proyectosEnRiesgo);
        model.addAttribute("listadoProyectosRetrasados", proyectosRetrasados);
        model.addAttribute("proyectosEnRiesgo", listadoProyectosEnRiesgo.size()); // El número para la tarjeta
        model.addAttribute("listadoProyectosEnRiesgo", listadoProyectosEnRiesgo); // La lista para el modal

        // ✅ NUEVA LÓGICA: Detectar sobrecarga de personal
        List<PersonalService.ReporteSobrecarga> personalEnRiesgo =
                personalService.detectarSobrecarga(inicioMes, finMes);

        model.addAttribute("personalEnRiesgo", personalEnRiesgo);
        model.addAttribute("cantRiesgoPersonal", personalEnRiesgo.size());

        return "principal";
    }
}