package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.model.Personal;
import com.cursoIntegradorI.proyectoFinal.service.PersonalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/personal")
@RequiredArgsConstructor
public class PersonalController {

    private final PersonalService personalService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("currentPage", "personal");
        model.addAttribute("personal", personalService.listarTodos());
        return "personal/lista";
    }

    /**
     * Crear nuevo usuario
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("personal", new Personal());
        return "personal/form";
    }

    /**
     *  Editar usuario por ID
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        return personalService.buscarPorId(id)
                .map(personal -> {
                    model.addAttribute("personal", personal);
                    return "personal/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Personal no encontrado");
                    return "redirect:/personal";
                });
    }

    /**
     * Guardar el usuario registrado
     */
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Personal personal,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        try {
            personalService.guardar(personal);
            redirectAttributes.addFlashAttribute("success", "Personal guardado exitosamente");
            return "redirect:/personal";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("personal", personal);
            return "personal/form";
        }
    }

    /**
     *  Eliminar usuarios por ID
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                           RedirectAttributes redirectAttributes) {
        try {
            personalService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Personal eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el personal");
        }
        return "redirect:/personal";
    }
}