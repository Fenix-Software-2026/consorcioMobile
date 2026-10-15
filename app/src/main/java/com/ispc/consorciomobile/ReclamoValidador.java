package com.ispc.consorciomobile;

public class ReclamoValidador {

        // Método para validar que el texto del reclamo no sea nulo, ni vacío, ni supere límites extraños
        public static boolean esValido(String texto) {
            if (texto == null) {
                return false;
            }
            // Quitamos espacios en blanco para evaluar si realmente tiene contenido
            String textoTrimmed = texto.trim();

            // Regla: No puede estar vacío y debe tener al menos 5 caracteres (ejemplo de valor límite)
            return !textoTrimmed.isEmpty() && textoTrimmed.length() >= 5;
        }
    }

