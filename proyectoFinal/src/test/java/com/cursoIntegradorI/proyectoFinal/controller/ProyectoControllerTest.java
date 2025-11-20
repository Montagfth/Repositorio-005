package com.cursoIntegradorI.proyectoFinal.controller;

import com.cursoIntegradorI.proyectoFinal.model.Cliente;
import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoService;
import com.cursoIntegradorI.proyectoFinal.service.ClienteService;
import com.cursoIntegradorI.proyectoFinal.service.ServicioService;
import com.cursoIntegradorI.proyectoFinal.service.PersonalService;
import com.cursoIntegradorI.proyectoFinal.service.ProyectoServicioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Pruebas Funcionales - ProyectoController")
class ProyectoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class MockConfig {

        @Bean
        ProyectoService proyectoService() {
            return Mockito.mock(ProyectoService.class);
        }

        @Bean
        ClienteService clienteService() {
            return Mockito.mock(ClienteService.class);
        }

        @Bean
        ServicioService servicioService() {
            return Mockito.mock(ServicioService.class);
        }

        @Bean
        PersonalService personalService() {
            return Mockito.mock(PersonalService.class);
        }

        @Bean
        ProyectoServicioService proyectoServicioService() {
            return Mockito.mock(ProyectoServicioService.class);
        }
    }

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private PersonalService personalService;

    @Autowired
    private ProyectoServicioService proyectoServicioService;

    private Proyecto proyecto;

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


    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe mostrar formulario de nuevo proyecto")
    void testMostrarFormularioNuevo() throws Exception {
        mockMvc.perform(get("/proyectos/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("proyectos/proyectos"))
                .andExpect(model().attributeExists("proyecto", "clientes"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe guardar un nuevo proyecto")
    void testGuardarProyecto() throws Exception {
        when(proyectoService.guardar(any(Proyecto.class))).thenReturn(proyecto);

        mockMvc.perform(post("/proyectos/guardar")
                        .param("nombre", "Proyecto Test")
                        .param("estado", "En Curso")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/proyectos/detalle/**"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe mostrar detalle del proyecto")
    void testVerDetalle() throws Exception {
        when(proyectoService.buscarPorId(1)).thenReturn(Optional.of(proyecto));
        when(proyectoServicioService.listarPorProyecto(1)).thenReturn(java.util.List.of());

        mockMvc.perform(get("/proyectos/detalle/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("proyectos/detalle"))
                .andExpect(model().attributeExists("proyecto", "serviciosProyecto"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe retornar 404 cuando proyecto no existe")
    void testProyectoNoEncontrado() throws Exception {
        when(proyectoService.buscarPorId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/proyectos/detalle/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/principal"));
    }
}