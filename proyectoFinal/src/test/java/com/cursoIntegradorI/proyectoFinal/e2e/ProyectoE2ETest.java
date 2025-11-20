package com.cursoIntegradorI.proyectoFinal.e2e;

import com.cursoIntegradorI.proyectoFinal.model.Cliente;
import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoService;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoServicioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Pruebas MVC - ProyectoController y Login")
class ProyectoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProyectoService proyectoService;

    @MockitoBean
    private ProyectoServicioService proyectoServicioService;

    private Proyecto proyecto;

    // =====================================================
    // SETUP GENERAL PARA TODAS LAS PRUEBAS
    // =====================================================
    @BeforeEach
    void setUp() {
        proyecto = new Proyecto();
        proyecto.setIdProyecto(1);
        proyecto.setNombre("Proyecto Test");
        proyecto.setEstado("En Curso");
        proyecto.setFechaInicio(LocalDate.now());

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1);
        cliente.setNombre("Cliente Test");
        proyecto.setCliente(cliente);

    }

    // =====================================================
    // LOGIN
    // =====================================================
    @Test
    @WithMockUser(username = "admin", roles = "USER")
    @DisplayName("Un usuario autenticado puede acceder a /principal")
    void testAccesoAutenticado() throws Exception {

        mockMvc.perform(get("/principal"))
                .andExpect(status().isOk());
    }



    @Test
    @DisplayName("Login fallido debe redirigir con error")
    void testLoginFallido() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "usuario")
                        .param("password", "incorrecto")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    // =====================================================
    // FORMULARIO NUEVO PROYECTO
    // =====================================================
    @Test
    @WithMockUser
    @DisplayName("Debe mostrar el formulario de nuevo proyecto")
    void testMostrarFormularioNuevoProyecto() throws Exception {
        mockMvc.perform(get("/proyectos/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("proyectos/proyectos"))
                .andExpect(model().attributeExists("proyecto"));
    }

    // =====================================================
    // GUARDAR PROYECTO
    // =====================================================
    @Test
    @WithMockUser
    @DisplayName("Debe guardar el proyecto correctamente")
    void testGuardarProyecto() throws Exception {

        Proyecto guardado = new Proyecto();
        guardado.setIdProyecto(10);
        guardado.setNombre("Proyecto Test");
        guardado.setEstado("En Curso");
        guardado.setFechaInicio(LocalDate.now());

        when(proyectoService.guardar(any())).thenReturn(guardado);

        mockMvc.perform(post("/proyectos/guardar")
                        .param("nombre", "Proyecto Test")
                        .param("estado", "En Curso")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/proyectos/detalle/10"));
    }

    // =====================================================
    // DETALLE DEL PROYECTO
    // =====================================================
    @Test
    @WithMockUser
    @DisplayName("Debe mostrar detalle del proyecto")
    void testVerDetalleProyecto() throws Exception {

        when(proyectoService.buscarPorId(1)).thenReturn(Optional.of(proyecto));
        when(proyectoServicioService.listarPorProyecto(1)).thenReturn(List.of());

        mockMvc.perform(get("/proyectos/detalle/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("proyectos/detalle"))
                .andExpect(model().attributeExists("proyecto"))
                .andExpect(model().attributeExists("serviciosProyecto"));
    }

    // =====================================================
    // PROYECTO NO ENCONTRADO
    // =====================================================
    @Test
    @WithMockUser
    @DisplayName("Debe redirigir a /principal cuando el proyecto no existe")
    void testProyectoNoEncontrado() throws Exception {

        when(proyectoService.buscarPorId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/proyectos/detalle/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/principal"));
    }
}
