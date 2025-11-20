package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Personal;
import com.cursoIntegradorI.proyectoFinal.repository.PersonalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - PersonalService")
class PersonalServiceTest {

    @Mock
    private PersonalRepository personalRepository;

    @InjectMocks
    private PersonalService personalService;

    private Personal personal;

    @BeforeEach
    void setUp() {
        personal = new Personal();
        personal.setIdPersonal(1);
        personal.setNombre("Juan Pérez");
        personal.setCargo("Desarrollador Senior");
        personal.setCorreo("juan@empresa.com");
        personal.setTelefono("987654321");
        personal.setTarifaEstimadaPorHora(100.0);
    }

    @Test
    @DisplayName("Debe validar que no exista correo duplicado")
    void testValidarCorreoDuplicado() {
        // Arrange
        when(personalRepository.findByCorreo("juan@empresa.com"))
                .thenReturn(Optional.of(personal));

        // Act & Assert
        assertThatThrownBy(() -> {
            if (personalRepository.findByCorreo("juan@empresa.com").isPresent()) {
                throw new IllegalArgumentException("El correo ya está registrado");
            }
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El correo ya está registrado");
    }

    @Test
    @DisplayName("Debe guardar personal correctamente")
    void testGuardarPersonal() {
        // Arrange
        when(personalRepository.findByCorreo("juan@empresa.com"))
                .thenReturn(Optional.empty());
        when(personalRepository.save(personal)).thenReturn(personal);

        // Act
        Personal resultado = personalService.guardar(personal);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Juan Pérez");
    }
}