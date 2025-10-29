package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoService;
import com.cursoIntegradorI.proyectoFinal.service.ClienteService;
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

    /**
     * Lista todos los proyectos
     */
    @GetMapping
    public String listar(Model model) {
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
        return "proyectos";
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
                    return "proyectos";
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
            proyectoService.guardar(proyecto);
            redirectAttributes.addFlashAttribute("success", "Proyecto guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el proyecto: " + e.getMessage());
        }
        return "redirect:/principal";
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