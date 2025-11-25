package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.model.ProyectoServicio;
import com.cursoIntegradorI.proyectoFinal.model.Servicio;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoServicioService;
import com.cursoIntegradorI.proyectoFinal.service.ServicioService;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;
    private final ProyectoServicioService proyectoServicioService;

    /**
     * GESTIÓN INDEPENDIENTE DE SERVICIOS (Catálogo)
     */

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("currentPage", "servicios");
        model.addAttribute("servicios", servicioService.listarCatalogo());
        return "servicios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("currentPage", "servicios");
        model.addAttribute("servicio", new Servicio());
        return "servicios/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model,
            RedirectAttributes redirectAttributes) {
        return servicioService.buscarPorId(id)
                .map(servicio -> {
                    model.addAttribute("currentPage", "servicios");
                    model.addAttribute("servicio", servicio);
                    return "servicios/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Servicio no encontrado");
                    return "redirect:/servicios";
                });
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Servicio servicio, RedirectAttributes redirectAttributes) {
        try {
            servicioService.guardarEnCatalogo(servicio);
            redirectAttributes.addFlashAttribute("success", "Servicio guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el servicio");
        }
        return "redirect:/servicios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            servicioService.eliminarDelCatalogo(id);
            redirectAttributes.addFlashAttribute("success", "Servicio eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el servicio. Puede estar en uso.");
        }
        return "redirect:/servicios";
    }

    /**
     * VINCULACIÓN DE SERVICIO A PROYECTO (desde detalle del proyecto)
     */

    @PostMapping("/vincular")
    public String vincularServicioAProyecto(@RequestParam Integer idServicio,
                                            @RequestParam Integer idProyecto,
                                            RedirectAttributes redirectAttributes) {

        try {
            // Usa el metodo correcto ya implementado en el service
            proyectoServicioService.asignarServicioAProyecto(
                    idProyecto,
                    idServicio,
                    null,        // costoAcordado (null = usar costoBase del catálogo)
                    null         // observaciones iniciales
            );

            redirectAttributes.addFlashAttribute("success",
                    "Servicio vinculado al proyecto exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al vincular servicio: " + e.getMessage());
        }

        return "redirect:/proyectos/detalle/" + idProyecto;
    }

    @GetMapping("/desvincular/{id}")
    public String desvincularServicioDeProyecto(@PathVariable Integer id,
                                                @RequestParam Integer idProyecto,
                                                RedirectAttributes redirectAttributes) {
        try {
            servicioService.eliminarDelCatalogo(id);
            redirectAttributes.addFlashAttribute("success", "Servicio desvinculado del proyecto");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo desvincular el servicio");
        }
        return "redirect:/proyectos/detalle/" + idProyecto;
    }

}

