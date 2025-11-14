package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.service.PersonalService;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoService;
import com.cursoIntegradorI.proyectoFinal.service.ClienteService;
import com.cursoIntegradorI.proyectoFinal.service.ServicioService;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;
    private final ClienteService clienteService;
    private final ServicioService servicioService;
    private final PersonalService personalService;
    private final ProyectoServicioService proyectoServicioService;  // ✅ NUEVO

    /**
     * Lista todos los proyectos
     */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("currentPage", "proyectos");
        model.addAttribute("proyectos", proyectoService.listarTodos());
        return "proyectos/lista";
    }

    /**
     * Muestra el formulario para crear un nuevo proyecto
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("clientes", clienteService.listarTodos());
        return "proyectos/proyectos";
    }

    /**
     * Muestra el formulario para editar un proyecto existente
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return proyectoService.buscarPorId(id)
                .map(proyecto -> {
                    model.addAttribute("proyecto", proyecto);
                    model.addAttribute("clientes", clienteService.listarTodos());
                    return "proyectos/proyectos";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Proyecto no encontrado");
                    return "redirect:/principal";
                });
    }

    /**
     * Guarda un proyecto (nuevo o actualizado)
     */
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Proyecto proyecto, RedirectAttributes redirectAttributes) {
        try {
            Proyecto proyectoGuardado = proyectoService.guardar(proyecto);
            redirectAttributes.addFlashAttribute("success", "Proyecto guardado exitosamente");
            // Redirigir al detalle del proyecto
            return "redirect:/proyectos/detalle/" + proyectoGuardado.getIdProyecto();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el proyecto: " + e.getMessage());
            return "redirect:/principal";
        }
    }

    /**
     * ✅ ACTUALIZADO: Muestra el detalle completo de un proyecto con sus servicios y asignaciones
     */
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return proyectoService.buscarPorId(id)
                .map(proyecto -> {
                    model.addAttribute("currentPage", "proyectos");
                    model.addAttribute("proyecto", proyecto);

                    // ✅ NUEVO: ProyectoServicios (servicios ya asignados a este proyecto)
                    model.addAttribute("serviciosProyecto", proyectoServicioService.listarPorProyecto(id));

                    // ✅ NUEVO: Catálogo completo de servicios disponibles
                    model.addAttribute("catalogoServicios", servicioService.listarCatalogo());

                    // Personal disponible para asignar
                    model.addAttribute("personalDisponible", personalService.listarTodos());

                    return "proyectos/detalle";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Proyecto no encontrado");
                    return "redirect:/principal";
                });
    }

    /**
     * ✅ ACTUALIZADO: Asigna un servicio del catálogo a este proyecto
     * Crea una entrada en la tabla intermedia ProyectoServicio
     */
    @PostMapping("/{id}/asignar-servicio")
    public String asignarServicioAProyecto(
            @PathVariable("id") Integer idProyecto,
            @RequestParam("idServicio") Integer idServicio,
            @RequestParam(value = "costoAcordado", required = false) Double costoAcordado,
            @RequestParam(value = "observaciones", required = false) String observaciones,
            RedirectAttributes redirect
    ) {
        try {
            proyectoServicioService.asignarServicioAProyecto(idProyecto, idServicio, costoAcordado, observaciones);
            redirect.addFlashAttribute("success", "Servicio asignado correctamente al proyecto");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al asignar servicio: " + e.getMessage());
        }

        return "redirect:/proyectos/detalle/" + idProyecto;
    }

    /**
     * ✅ NUEVO: Desvincula un servicio del proyecto
     * Elimina la relación ProyectoServicio (y sus asignaciones en cascada)
     */
    @GetMapping("/{idProyecto}/desvincular-servicio/{idProyectoServicio}")
    public String desvincularServicio(
            @PathVariable Integer idProyecto,
            @PathVariable Integer idProyectoServicio,
            RedirectAttributes redirect
    ) {
        try {
            proyectoServicioService.eliminar(idProyectoServicio);
            redirect.addFlashAttribute("success", "Servicio desvinculado del proyecto");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al desvincular servicio: " + e.getMessage());
        }

        return "redirect:/proyectos/detalle/" + idProyecto;
    }

    /**
     * ✅ NUEVO: Actualiza datos específicos de un servicio en el proyecto
     * (costo acordado, observaciones, estado)
     */
    @PostMapping("/{idProyecto}/actualizar-servicio/{idProyectoServicio}")
    public String actualizarServicioProyecto(
            @PathVariable Integer idProyecto,
            @PathVariable Integer idProyectoServicio,
            @RequestParam Double costoAcordado,
            @RequestParam(required = false) String observaciones,
            @RequestParam String estado,
            RedirectAttributes redirect
    ) {
        try {
            proyectoServicioService.actualizar(idProyectoServicio, costoAcordado, observaciones, estado);
            redirect.addFlashAttribute("success", "Servicio actualizado correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al actualizar servicio: " + e.getMessage());
        }

        return "redirect:/proyectos/detalle/" + idProyecto;
    }

    /**
     * Elimina un proyecto
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            proyectoService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Proyecto eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el proyecto. Puede tener dependencias.");
        }
        return "redirect:/principal";
    }

    /**
     * Busca proyectos por estado
     */
    @GetMapping("/estado/{estado}")
    public String buscarPorEstado(@PathVariable String estado, Model model) {
        model.addAttribute("proyectos", proyectoService.buscarPorEstado(estado));
        model.addAttribute("estadoFiltro", estado);
        return "proyectos/lista";
    }

    /**
     * Busca proyectos por cliente
     */
    @GetMapping("/cliente/{idCliente}")
    public String buscarPorCliente(@PathVariable Integer idCliente, Model model) {
        model.addAttribute("proyectos", proyectoService.buscarPorCliente(idCliente));
        return "proyectos/lista";
    }
}