package com.ispc.consorciomobile;

import com.google.gson.annotations.SerializedName;

public class Comunicado {

    private int id;
    private String titulo;
    private String contenido;

    @SerializedName("fecha_publicacion")
    private String fechaPublicacion;

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }
}
