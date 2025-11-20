package com.cursoIntegradorI.proyectoFinal.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
@DisplayName("Pruebas de Seguridad - Autenticación y Autorización")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Debe redireccionar a login sin autenticación")
    void testAccesoSinAutenticacion() throws Exception {
        mockMvc.perform(get("/principal"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("Debe permitir acceso al login sin autenticación")
    void testAccesoAlLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    @DisplayName("Debe permitir acceso con usuario autenticado")
    void testAccesoConAutenticacion() throws Exception {
        mockMvc.perform(get("/principal"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Debe rechazar CSRF sin token")
    void testCSRFProtection() throws Exception {
        mockMvc.perform(post("/proyectos/guardar")
                        .param("nombre", "Test"))
                .andExpect(status().isForbidden());
    }



    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe permitir POST con CSRF token válido")
    void testCSRFConTokenValido() throws Exception {
        mockMvc.perform(post("/clientes/guardar")
                        .param("nombre", "Test Cliente")
                        .param("ruc", "20123456789")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Debe validar contraseña incorrecta")
    void testLoginConCredencialesInvalidas() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "admin")
                        .param("password", "wrongpassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }
}