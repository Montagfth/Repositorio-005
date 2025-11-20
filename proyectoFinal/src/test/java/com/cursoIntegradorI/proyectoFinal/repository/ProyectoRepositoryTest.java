package com.cursoIntegradorI.proyectoFinal.repository;

import com.cursoIntegradorI.proyectoFinal.model.Proyecto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Pruebas de Repositorio - ProyectoRepository")
class ProyectoRepositoryTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test_db")
            .withUsername("root")
            .withPassword("root");

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Test
    @DisplayName("Debe guardar y recuperar un proyecto de la BD")
    void testGuardarYRecuperarProyecto() {
        // Arrange
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Test BD");
        proyecto.setEstado("En Curso");
        proyecto.setFechaInicio(LocalDate.now());

        // Act
        Proyecto guardado = proyectoRepository.save(proyecto);

        // Assert
        assertThat(guardado.getIdProyecto()).isNotNull();
        assertThat(guardado.getNombre()).isEqualTo("Test BD");
    }

    @Test
    @DisplayName("Debe filtrar proyectos por estado")
    void testFindByEstado() {
        // Arrange
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Test Estado");
        proyecto.setEstado("En Curso");
        proyectoRepository.save(proyecto);

        // Act
        List<Proyecto> resultados = proyectoRepository.findByEstado("En Curso");

        // Assert
        assertThat(resultados)
                .isNotEmpty()
                .anyMatch(p -> p.getNombre().equals("Test Estado"));
    }
}