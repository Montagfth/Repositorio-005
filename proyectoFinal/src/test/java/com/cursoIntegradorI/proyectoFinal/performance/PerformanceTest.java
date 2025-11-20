package com.cursoIntegradorI.proyectoFinal.performance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Pruebas de Rendimiento")
class PerformanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    @Test
    @DisplayName("Debe cargar lista de proyectos en < 1000ms")
    void testPerformanceListaProyectos() {
        long inicio = System.currentTimeMillis();

        get("/proyectos")
                .then()
                .statusCode(anyOf(is(200), is(302)));

        long duracion = System.currentTimeMillis() - inicio;
        assert duracion < 1000 : "La respuesta tardó más de 1000ms";
    }

    @Test
    @DisplayName("Debe manejar 100 solicitudes concurrentes")
    void testCargaConcurrente() throws InterruptedException {
        int numThreads = 100;
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                get("/proyectos")
                        .then()
                        .statusCode(anyOf(is(200), is(302)));
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}