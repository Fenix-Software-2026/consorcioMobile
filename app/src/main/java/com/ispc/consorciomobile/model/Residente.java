package com.ispc.consorciomobile.model;

import com.google.gson.annotations.SerializedName;

public class Residente {
    private int id;
    private String username;
    private String email;
    private  String nombre;
    private  String apellido;
    private  Integer unidad;
    @SerializedName("unidad_detalle")
    private Unidad unidadDetalle;

    public Residente(String username, String email, String nombre, String apellido, int unidad) {
        this.username = username;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.unidad = unidad;
    }

    //getter y setters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public Integer getUnidad() { return unidad; }
    public Unidad getUnidadDetalle() { return unidadDetalle; }

    // Nombre y apellido para mostrar en la interfaz, sin "null" si la API no los trae.
    public String getNombreCompleto() {
        String nombre = this.nombre == null ? "" : this.nombre.trim();
        String apellido = this.apellido == null ? "" : this.apellido.trim();
        String completo = (nombre + " " + apellido).trim();
        return completo.isEmpty() ? (username == null ? "" : username) : completo;
    }

}
