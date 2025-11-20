package com.cursoIntegradorI.proyectoFinal.service;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import com.cursoIntegradorI.proyectoFinal.repository.ProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - ProyectoService")
class ProyectoServiceTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private ProyectoService proyectoService;

    private Proyecto proyecto;

    @BeforeEach
    void setUp() {
        proyecto = new Proyecto();
        proyecto.setIdProyecto(1);
        proyecto.setNombre("Proyecto de Prueba");
        proyecto.setDescripcion("Descripción de prueba");
        proyecto.setEstado("En Curso");
        proyecto.setFechaInicio(LocalDate.now());
        proyecto.setFechaFin(LocalDate.now().plusMonths(3));
        proyecto.setPresupuestoTotal(5000.0);
    }

    @Test
    @DisplayName("Debe guardar un proyecto correctamente")
    void testGuardarProyecto() {
        // Arrange
        when(proyectoRepository.save(any(Proyecto.class)))
                .thenReturn(proyecto);

        // Act
        Proyecto resultado = proyectoService.guardar(proyecto);

        // Assert
        assertThat(resultado)
                .isNotNull()
                .extracting(Proyecto::getNombre)
                .isEqualTo("Proyecto de Prueba");

        verify(proyectoRepository, times(1)).save(proyecto);
    }

    @Test
    @DisplayName("Debe listar todos los proyectos")
    void testListarTodos() {
        // Arrange
        List<Proyecto> proyectos = List.of(proyecto);
        when(proyectoRepository.findAll()).thenReturn(proyectos);

        // Act
        List<Proyecto> resultado = proyectoService.listarTodos();

        // Assert
        assertThat(resultado)
                .isNotEmpty()
                .hasSize(1)
                .contains(proyecto);
    }

    @Test
    @DisplayName("Debe buscar proyecto por ID")
    void testBuscarPorId() {
        // Arrange
        when(proyectoRepository.findById(1)).thenReturn(Optional.of(proyecto));

        // Act
        Optional<Proyecto> resultado = proyectoService.buscarPorId(1);

        // Assert
        assertThat(resultado)
                .isPresent()
                .contains(proyecto);
    }

    @Test
    @DisplayName("Debe filtrar proyectos por estado")
    void testBuscarPorEstado() {
        // Arrange
        List<Proyecto> proyectosEnCurso = List.of(proyecto);
        when(proyectoRepository.findByEstado("En Curso"))
                .thenReturn(proyectosEnCurso);

        // Act
        List<Proyecto> resultado = proyectoService.buscarPorEstado("En Curso");

        // Assert
        assertThat(resultado)
                .isNotEmpty()
                .allMatch(p -> p.getEstado().equals("En Curso"));
    }

    @Test
    @DisplayName("Debe eliminar un proyecto")
    void testEliminar() {
        // Act
        proyectoService.eliminar(1);

        // Assert
        verify(proyectoRepository, times(1)).deleteById(1);
    }
}