package com.ispc.consorciomobile;

/**
 * Validador de formato y robustez de contraseñas.
 * Criterio requerido:
 * - Mínimo 8 caracteres
 * - Al menos una letra mayúscula
 * - Al menos una letra minúscula
 * - Al menos un dígito numérico
 * - Al menos un carácter especial (@#$%^&+=!_.-)
 * - Sin espacios en blanco
 */
public final class ValidadorContrasena {

    public static final String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_.-])(?=\\S+$).{8,}$";

    private ValidadorContrasena() {
        // Constructor privado para evitar que la clase sea instanciada.
        // Se utiliza únicamente a través de sus métodos estáticos.
    }

    /**
     * Evalúa si una contraseña cumple con todas las reglas de negocio y seguridad.
     *
     * @param contrasena La cadena a validar.
     * @return true si la contraseña cumple con el formato y longitud; false si es nula, vacía o inválida.
     */
    public static boolean esValida(String contrasena) {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            return false;
        }
        return contrasena.matches(PASSWORD_REGEX);
    }
}