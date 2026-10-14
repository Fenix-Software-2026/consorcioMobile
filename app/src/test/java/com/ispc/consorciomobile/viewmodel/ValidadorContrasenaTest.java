package com.ispc.consorciomobile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Pruebas unitarias sobre ValidadorContrasena diseñadas mediante
 * Clases de Equivalencia y Análisis de Valores Límite.
 * Ejecución: JVM Local (app/src/test/java/).
 */
public class ValidadorContrasenaTest {

    // -------------------------------------------------------------------------
    // TEST 1: VALOR VÁLIDO (Clase de Equivalencia Válida)
    // Trazable a: TC-VAL-001
    // Criterio: Contraseña con longitud adecuada y todos los caracteres requeridos.
    // -------------------------------------------------------------------------
    @Test
    public void contrasena_valorValido_retornaTrue() {
        // Arrange (Preparación)
        String contrasenaValida = "Consorcio2026!";

        // Act (Ejecución)
        boolean resultado = ValidadorContrasena.esValida(contrasenaValida);

        // Assert (Comprobación)
        assertTrue("Una contraseña con formato y longitud válidos debe ser aceptada", resultado);
    }

    // -------------------------------------------------------------------------
    // TEST 2: CAMPO VACÍO (Clase de Equivalencia Inválida)
    // Trazable a: TC-VAL-002
    // Criterio: Cadenas vacías, nulas o con solo espacios no deben ser aceptadas.
    // -------------------------------------------------------------------------
    @Test
    public void contrasena_campoVacioONulo_retornaFalse() {
        // Arrange & Act & Assert
        // Caso A: Cadena totalmente vacía ("")
        assertFalse("El campo vacío no debe ser permitido",
                ValidadorContrasena.esValida(""));

        // Caso B: Solo espacios en blanco ("   ")
        assertFalse("Una cadena de solo espacios no debe ser permitida",
                ValidadorContrasena.esValida("   "));

        // Caso C: Valor nulo
        assertFalse("Un valor null no debe ser permitido",
                ValidadorContrasena.esValida(null));
    }

    // -------------------------------------------------------------------------
    // TEST 3: VALOR JUSTO EN EL LÍMITE (Análisis de Valores Límite - BVA)
    // Trazable a: TC-VAL-003
    // Criterio: La longitud mínima requerida es 8 caracteres.
    // - Frontera válida (8 caracteres): debe retornar true.
    // - Frontera inválida (7 caracteres): debe retornar false.
    // -------------------------------------------------------------------------
    @Test
    public void contrasena_valoresEnElLimite_validaCorrectamente() {
        // Límite exacto permitido: 8 caracteres (A-b-c-1-2-3-!-@)
        String justoEnElLimite = "Abc123!@";
        assertTrue("Exactamente 8 caracteres válidos debe ser aceptado",
                ValidadorContrasena.esValida(justoEnElLimite));

        // Un caracter por debajo del límite: 7 caracteres (A-b-1-2-3-!-@)
        String justoFueraDelLimite = "Ab123!@";
        assertFalse("7 caracteres (un caracter menos del mínimo) debe ser rechazado",
                ValidadorContrasena.esValida(justoFueraDelLimite));
    }

    // -------------------------------------------------------------------------
    // TEST 4: VALOR FUERA DE FORMATO (Clase de Equivalencia Inválida)
    // Trazable a: TC-VAL-004
    // Criterio: Tiene longitud suficiente (>= 8 caracteres) pero incumple la estructura exigida.
    // -------------------------------------------------------------------------
    @Test
    public void contrasena_fueraDeFormato_retornaFalse() {
        // Caso A: Falta mayúscula (solo minúsculas, número y símbolo)
        String sinMayuscula = "password123!";
        assertFalse("Debe fallar si no tiene al menos una letra mayúscula",
                ValidadorContrasena.esValida(sinMayuscula));

        // Caso B: Falta número
        String sinNumero = "PasswordSinNumero!";
        assertFalse("Debe fallar si no tiene al menos un número",
                ValidadorContrasena.esValida(sinNumero));

        // Caso C: Falta carácter especial
        String sinEspecial = "PasswordSegura123";
        assertFalse("Debe fallar si no tiene un carácter especial",
                ValidadorContrasena.esValida(sinEspecial));

        // Caso D: Contiene espacios en blanco en medio
        String conEspacio = "Pass 1234!";
        assertFalse("Debe fallar si contiene espacios en blanco",
                ValidadorContrasena.esValida(conEspacio));
    }
}