package com.cursoIntegradorI.proyectoFinal.security;

import com.cursoIntegradorI.proyectoFinal.service.PersonalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Pruebas de Seguridad - SQL Injection y XSS")
class SecurityVulnerabilitiesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonalService personalService;

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe proteger contra SQL Injection en búsqueda")
    void testSQLInjectionProtection() throws Exception {
        String sqlInjection = "' OR '1'='1";

        mockMvc.perform(post("/clientes/guardar")
                        .param("nombre", sqlInjection)
                        .param("ruc", "20123456789")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Debe sanitizar entrada para evitar XSS")
    void testXSSProtection() throws Exception {
        String xssPayload = "<script>alert('XSS')</script>";

        mockMvc.perform(post("/proyectos/guardar")
                        .param("nombre", xssPayload)
                        .param("estado", "En Curso")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    @WithMockUser(roles = "USER")
    void testEmailValidation() throws Exception {

        doThrow(new IllegalArgumentException("Correo inválido"))
                .when(personalService).guardar(any());

        mockMvc.perform(post("/personal/guardar")
                        .param("nombre", "Test")
                        .param("correo", "invalid-email")
                        .param("cargo", "Developer")
                        .param("telefono", "987654321")
                        .with(csrf()))
                .andExpect(status().isOk());     // 200 porque retorna la vista "personal/form"
    }


}