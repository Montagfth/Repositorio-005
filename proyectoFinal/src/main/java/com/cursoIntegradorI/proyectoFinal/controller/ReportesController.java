package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.service.ProyectoService;
import com.cursoIntegradorI.proyectoFinal.service.RequerimientoService;
import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReportesController {

    private final ProyectoService proyectoService;
    private final RequerimientoService requerimientoService;

    @GetMapping
    public String mostrarReportes(Model model) {
        model.addAttribute("currentPage", "reportes");
        List<Proyecto> todosLosProyectos = proyectoService.listarTodos();

        // Calcular métricas generales
        long proyectosEnProgreso = todosLosProyectos.stream()
                .filter(p -> "En Curso".equalsIgnoreCase(p.getEstado()) ||
                        "Planificado".equalsIgnoreCase(p.getEstado()))
                .count();

        // Calcular porcentaje de tareas completadas (simulado)
        long totalRequerimientos = requerimientoService.listarTodos().size();
        long requerimientosValidados = requerimientoService.buscarPorValidado(true).size();
        int porcentajeTareasCompletadas = totalRequerimientos > 0
                ? (int) ((requerimientosValidados * 100.0) / totalRequerimientos)
                : 0;

        // Notificaciones recientes (simulado - últimos 5 proyectos modificados)
        int notificacionesRecientes = Math.min(5, todosLosProyectos.size());

        // Distribución de proyectos por estado
        Map<String, Long> proyectosPorEstado = todosLosProyectos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getEstado() != null ? p.getEstado() : "Sin Estado",
                        Collectors.counting()
                ));

        // Proyectos recientes (últimos 5)
        List<Proyecto> proyectosRecientes = todosLosProyectos.stream()
                .limit(5)
                .collect(Collectors.toList());

        // Agregar datos al modelo
        model.addAttribute("proyectosEnProgreso", proyectosEnProgreso);
        model.addAttribute("porcentajeTareasCompletadas", porcentajeTareasCompletadas);
        model.addAttribute("notificacionesRecientes", notificacionesRecientes);
        model.addAttribute("proyectosPorEstado", proyectosPorEstado);
        model.addAttribute("proyectosRecientes", proyectosRecientes);
        model.addAttribute("totalProyectos", todosLosProyectos.size());

        return "Reports";
    }
}