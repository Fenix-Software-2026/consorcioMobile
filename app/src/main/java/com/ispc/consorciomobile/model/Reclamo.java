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

    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public void setUnidad(int unidad) {
        this.unidad = unidad;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
}
