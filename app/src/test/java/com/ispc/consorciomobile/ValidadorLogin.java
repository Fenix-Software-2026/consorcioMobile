package com.ispc.consorciomobile;

/**
 * Validador de reglas para los campos del formulario de Login.
 */
public final class ValidadorLogin {

    private ValidadorLogin() {

    }

    /**
     * Valida que el nombre de usuario no sea nulo, ni vacío, ni solo espacios,
     * y que tenga al menos 3 caracteres.
     */
    public static boolean esUsuarioValido(String usuario) {
        if (usuario == null) return false;
        String limpio = usuario.trim();
        return limpio.length() >= 3 && !limpio.contains(" ");
    }

    /**
     * Valida que la contraseña no sea nula, ni vacía, ni solo espacios.
     */
    public static boolean esPasswordValida(String password) {
        return password != null && !password.trim().isEmpty();
    }

    /**
     * Valida que ambos campos obligatorios estén presentes y sean válidos.
     */
    public static boolean validarFormulario(String usuario, String password) {
        return esUsuarioValido(usuario) && esPasswordValida(password);
    }
}