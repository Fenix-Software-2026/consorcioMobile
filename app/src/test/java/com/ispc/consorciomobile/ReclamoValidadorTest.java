package com.ispc.consorciomobile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReclamoValidadorTest {

    @Test
    public void validarTextoReclamo_valorValido_retornaTrue() {
        // // Arrange
        String texto = "Rotura de luces en palier";

        // // Act
        boolean resultado = ReclamoValidador.esValido(texto);

        // // Assert
        assertTrue(resultado);
    }

    @Test
    public void validarTextoReclamo_valorVacio_retornaFalse() {
        // // Arrange
        String texto = "";

        // // Act
        boolean resultado = ReclamoValidador.esValido(texto);

        // // Assert
        assertFalse(resultado);
    }


}
