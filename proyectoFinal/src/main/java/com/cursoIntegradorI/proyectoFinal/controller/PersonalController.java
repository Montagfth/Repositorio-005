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
        model.addAttribute("personal", personalService.listarTodos());
        return "personal/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("personal", new Personal());
        return "personal/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
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

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Personal personal, RedirectAttributes redirectAttributes) {
        personalService.guardar(personal);
        redirectAttributes.addFlashAttribute("success", "Personal guardado exitosamente");
        return "redirect:/personal";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            personalService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Personal eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el personal");
        }
        return "redirect:/personal";
    }
}