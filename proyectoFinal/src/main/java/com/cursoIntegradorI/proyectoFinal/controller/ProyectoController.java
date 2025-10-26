package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoService;
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
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping("/principal")
    public String mostrarCalendario(@RequestParam(defaultValue = "0") int offset, Model model) {
        YearMonth mesActual = YearMonth.now().plusMonths(offset);
        LocalDate primerDia = mesActual.atDay(1);
        int diasEnMes = mesActual.lengthOfMonth();

        List<LocalDate> dias = IntStream.rangeClosed(1, diasEnMes)
                .mapToObj(mesActual::atDay)
                .collect(Collectors.toList());

        List<Proyecto> proyectos = proyectoService.listarTodos();

        // ✅ Forma moderna sin usar "new Locale()"
        String nombreMes = mesActual.getMonth()
                .getDisplayName(TextStyle.FULL, Locale.of("es", "ES"));
        nombreMes = Character.toUpperCase(nombreMes.charAt(0)) + nombreMes.substring(1);

        model.addAttribute("offset", offset);
        model.addAttribute("nombreMes", nombreMes);
        model.addAttribute("año", mesActual.getYear());
        model.addAttribute("dias", dias);
        model.addAttribute("proyectos", proyectos);

        return "principal";
    }
}
