package com.ispc.consorciomobile;

import com.google.gson.annotations.SerializedName;

public class Comunicado {

    private int id;
    private String titulo;
    private String contenido;

    @SerializedName("fecha_publicacion")
    private String fechaPublicacion;

    // Marca si el comunicado se debe resaltar en la lista (fondo naranja).
    @SerializedName("es_urgente")
    private boolean esUrgente;

    // Usuario que publicó el comunicado. La API lo devuelve de solo lectura
    // y puede venir en null si el autor ya no está en el sistema.
    private Integer usuario;

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

    public boolean getEsUrgente() {
        return esUrgente;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public void setEsUrgente(boolean esUrgente) {
        this.esUrgente = esUrgente;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }
}
