package com.ispc.consorciomobile;

import static org.junit.Assert.assertEquals;

import com.ispc.consorciomobile.model.Reclamo;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ContadorReclamosTest {

    @Test
    public void listaVacia_devuelveCero() {
        // Arrange: preparar los datos.
        List<Reclamo> reclamos = Collections.emptyList();

        // Act: ejecutar el cálculo.
        int resultado = ContadorReclamos.contarActivos(reclamos);

        // Assert: comprobar el resultado.
        assertEquals(0, resultado);
    }

    @Test
    public void pendienteYEnProceso_cuentaAmbos() {
        // Arrange
        List<Reclamo> reclamos = Arrays.asList(
                crearReclamo("pendiente"),
                crearReclamo("en_proceso")
        );

        // Act
        int resultado = ContadorReclamos.contarActivos(reclamos);

        // Assert
        assertEquals(2, resultado);
    }

    @Test
    public void resueltoYArchivado_devuelveCero() {
        // Arrange
        List<Reclamo> reclamos = Arrays.asList(
                crearReclamo("resuelto"),
                crearReclamo("archivado")
        );

        // Act
        int resultado = ContadorReclamos.contarActivos(reclamos);

        // Assert
        assertEquals(0, resultado);
    }

    @Test
    public void estadosMezclados_cuentaSoloActivos() {
        // Arrange
        List<Reclamo> reclamos = Arrays.asList(
                crearReclamo("pendiente"),
                crearReclamo("resuelto"),
                crearReclamo("en_proceso"),
                crearReclamo("archivado"),
                crearReclamo("pendiente"),
                crearReclamo("resuelto")
        );

        // Act
        int resultado = ContadorReclamos.contarActivos(reclamos);

        // Assert
        assertEquals(3, resultado);
    }

    private Reclamo crearReclamo(String estado) {
        Reclamo reclamo = new Reclamo();
        reclamo.setEstado(estado);
        return reclamo;
    }
}