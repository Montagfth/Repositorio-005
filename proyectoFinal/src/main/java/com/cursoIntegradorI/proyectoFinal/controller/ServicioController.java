package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.model.Servicio;
import com.cursoIntegradorI.proyectoFinal.service.ServicioService;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;
    private final ProyectoService proyectoService;

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Servicio servicio,
                          @RequestParam Integer idProyecto,
                          RedirectAttributes redirectAttributes) {
        try {
            servicioService.guardar(servicio);
            redirectAttributes.addFlashAttribute("success", "Servicio agregado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar servicio");
        }
        return "redirect:/proyectos/detalle/" + idProyecto;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                           @RequestParam Integer idProyecto,
                           RedirectAttributes redirectAttributes) {
        try {
            servicioService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Servicio eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el servicio");
        }
        return "redirect:/proyectos/detalle/" + idProyecto;
    }
}