package com.ispc.consorciomobile.model;

import com.google.gson.annotations.SerializedName;

public class Reclamo {
    private int id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String estado;

    @SerializedName("imagen_url")
    private String imagenUrl;

    @SerializedName( "fecha_creacion")
    private String fechaCreacion;

    @SerializedName("fecha_actualizacion")
    private String fechaActualizacion;

    private int unidad;
    private int usuario;

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getEstado() {
        return estado;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public int getUnidad() {
        return unidad;
    }

    public int getUsuario() {
        return usuario;
    }
}
