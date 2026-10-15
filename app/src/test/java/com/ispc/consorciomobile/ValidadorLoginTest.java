package com.ispc.consorciomobile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidadorLoginTest {

    // -------------------------------------------------------------------------
    // TC-L-01: Credenciales válidas completas
    // -------------------------------------------------------------------------
    @Test
    public void TC_L_01_camposValidos_retornaTrue() {
        // Arrange
        String user = "residentePrueba1";
        String pass = "Contraseña";

        // Act
        boolean resultado = ValidadorLogin.validarFormulario(user, pass);

        // Assert
        assertTrue("Con usuario y contraseña válidos debe retornar true", resultado);
    }

    // -------------------------------------------------------------------------
    // TC-L-03: Intento de inicio de sesión con ambos campos vacíos
    // -------------------------------------------------------------------------
    @Test
    public void TC_L_03_ambosCamposVacios_retornaFalse() {
        // Arrange
        String userVacio = "";
        String passVacio = "";

        // Act & Assert
        assertFalse("Ambos campos vacíos deben ser rechazados",
                ValidadorLogin.validarFormulario(userVacio, passVacio));
    }

    // -------------------------------------------------------------------------
    // TC-L-04: Intento de inicio de sesión sin ingresar contraseña
    // -------------------------------------------------------------------------
    @Test
    public void TC_L_04_sinPassword_retornaFalse() {
        // Arrange
        String userValido = "residentePrueba1";
        String passVacio = "";

        // Act & Assert
        assertFalse("Sin contraseña debe rechazar la validación",
                ValidadorLogin.validarFormulario(userValido, passVacio));
    }

    // -------------------------------------------------------------------------
    // TC-L-05: Intento de inicio de sesión sin ingresar usuario
    // -------------------------------------------------------------------------
    @Test
    public void TC_L_05_sinUsuario_retornaFalse() {
        // Arrange
        String userVacio = "";
        String passValido = "Contraseña";

        // Act & Assert
        assertFalse("Sin usuario debe rechazar la validación",
                ValidadorLogin.validarFormulario(userVacio, passValido));
    }
}