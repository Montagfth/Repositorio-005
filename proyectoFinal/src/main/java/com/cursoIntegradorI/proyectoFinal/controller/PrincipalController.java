package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class PrincipalController {

    private final ProyectoService proyectoService;

    @GetMapping({"/", "/principal"})
    public String mostrarCalendario(@RequestParam(defaultValue = "0") int offset, Model model) {
        model.addAttribute("currentPage", "principal");
        YearMonth mesActual = YearMonth.now().plusMonths(offset);
        int diasEnMes = mesActual.lengthOfMonth();

        // Generar lista de días del mes
        List<LocalDate> dias = IntStream.rangeClosed(1, diasEnMes)
                .mapToObj(mesActual::atDay)
                .collect(Collectors.toList());

        // Obtener todos los proyectos para mostrar en el calendario
        List<Proyecto> proyectos = proyectoService.listarTodos();

        // Calcular estadísticas para el panel lateral
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

        // Agregar atributos al modelo
        model.addAttribute("offset", offset);
        model.addAttribute("nombreMes", nombreMes);
        model.addAttribute("año", mesActual.getYear());
        model.addAttribute("dias", dias);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("proyectosActivos", proyectosActivos);
        model.addAttribute("proyectosEnProgreso", proyectosEnProgreso);
        model.addAttribute("proyectosFinalizados", proyectosFinalizados);

        return "principal";
    }
}