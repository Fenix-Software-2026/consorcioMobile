package com.ispc.consorciomobile.network;

public class TokenManager {

    private static String tokenActual = "";
    private static String usernameActual = "";

    public static synchronized void guardarToken(String token) {
        tokenActual = token == null ? "" : token;
    }

    public static synchronized String obtenerToken() {
        return tokenActual;
    }

    public static synchronized boolean tieneToken() {
        return !tokenActual.isEmpty();
    }

    public static synchronized void limpiarToken() {
        tokenActual = "";
        usernameActual="";
    }

    public static synchronized void guardarUsername(String username) {
        usernameActual = username == null ? "" : username;
    }

    public static synchronized String obtenerUsername() {
        return usernameActual;
    }
}