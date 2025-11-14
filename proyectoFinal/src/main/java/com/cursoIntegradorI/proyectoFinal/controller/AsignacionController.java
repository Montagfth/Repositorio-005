package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.model.Asignacion;
import com.cursoIntegradorI.proyectoFinal.model.ProyectoServicio;
import com.cursoIntegradorI.proyectoFinal.service.AsignacionService;
import com.cursoIntegradorI.proyectoFinal.service.ServicioService;
import com.cursoIntegradorI.proyectoFinal.service.PersonalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/asignaciones")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;
    private final ServicioService servicioService;
    private final PersonalService personalService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("asignaciones", asignacionService.listarTodas());
        return "asignaciones/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("asignacion", new Asignacion());
        model.addAttribute("servicios", servicioService.listarCatalogo());
        model.addAttribute("personal", personalService.listarTodos());
        return "asignaciones/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return asignacionService.buscarPorId(id)
                .map(asignacion -> {
                    model.addAttribute("asignacion", asignacion);
                    model.addAttribute("servicios", servicioService.listarCatalogo());
                    model.addAttribute("personal", personalService.listarTodos());
                    return "asignaciones/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Asignación no encontrada");
                    return "redirect:/asignaciones";
                });
    }

    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Asignacion asignacion,
            @RequestParam("idProyecto") Integer idProyecto,
            @RequestParam("proyectoServicio.idProyectoServicio") Integer idProyectoServicio,
            RedirectAttributes redirectAttributes) {

        try {
            // 1. Vincular la asignación con el ProyectoServicio correcto
            ProyectoServicio ps = new ProyectoServicio();
            ps.setIdProyectoServicio(idProyectoServicio);
            asignacion.setProyectoServicio(ps);

            // 2. Guardar asignación
            asignacionService.guardar(asignacion);

            redirectAttributes.addFlashAttribute("success", "Personal asignado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al asignar personal");
        }

        // 3. Volver al detalle del proyecto
        return "redirect:/proyectos/detalle/" + idProyecto;
    }


    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            asignacionService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Asignación eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la asignación");
        }
        return "redirect:/asignaciones";
    }
}