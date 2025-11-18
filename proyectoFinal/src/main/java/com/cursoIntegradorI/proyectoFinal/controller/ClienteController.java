package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.model.Cliente;
import com.cursoIntegradorI.proyectoFinal.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("currentPage", "clientes");
        model.addAttribute("clientes", clienteService.listarTodos());
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("currentPage", "clientes");
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return clienteService.buscarPorId(id)
                .map(cliente -> {
                    model.addAttribute("currentPage", "clientes");
                    model.addAttribute("cliente", cliente);
                    return "clientes/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Cliente no encontrado");
                    return "redirect:/clientes";
                });
    }

    @PostMapping("/eliminar/{id}")  // ✅ Cambiar a POST
    public String eliminar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Cliente eliminado exitosamente");

        } catch (IllegalStateException e) {
            // Error específico: cliente tiene proyectos
            redirectAttributes.addFlashAttribute("error", e.getMessage());

        } catch (IllegalArgumentException e) {
            // Error: cliente no encontrado
            redirectAttributes.addFlashAttribute("error", e.getMessage());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo eliminar el cliente. Error: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes, Model model) {
        try {
            // ✅ Asegurarse de que la lista de proyectos sea null
            cliente.setProyectos(null);

            clienteService.guardar(cliente);

            String mensaje = cliente.getIdCliente() != null ?
                    "Cliente actualizado exitosamente" :
                    "Cliente creado exitosamente";

            redirectAttributes.addFlashAttribute("success", mensaje);
            return "redirect:/clientes";

        } catch (IllegalArgumentException e) {
            // Errores de validación (RUC o correo duplicado)
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cliente", cliente);
            model.addAttribute("currentPage", "clientes");
            return "clientes/form";

        } catch (Exception e) {
            model.addAttribute("error", "Error inesperado al guardar el cliente: " + e.getMessage());
            model.addAttribute("cliente", cliente);
            model.addAttribute("currentPage", "clientes");
            return "clientes/form";
        }
    }

    /**
     * Endpoint AJAX para verificar si un cliente se puede eliminar
     */
    @GetMapping("/verificar-eliminacion/{id}")
    @ResponseBody
    public java.util.Map<String, Object> verificarEliminacion(@PathVariable Integer id) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();

        try {
            long proyectosAsociados = clienteService.contarProyectosAsociados(id);  // ✅ Cambiar int a long
            response.put("puedeEliminar", proyectosAsociados == 0);
            response.put("proyectosAsociados", proyectosAsociados);
            response.put("mensaje", proyectosAsociados > 0 ?
                    "Este cliente tiene " + proyectosAsociados + " proyecto(s) asociado(s)" :
                    "Cliente puede ser eliminado");

        } catch (Exception e) {
            response.put("puedeEliminar", false);
            response.put("error", e.getMessage());
        }

        return response;
    }
}