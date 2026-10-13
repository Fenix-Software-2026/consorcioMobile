package com.ispc.consorciomobile.model;

public class Unidad {
    private int id;
    private String departamento;
    private String piso;

    public int getId() {
        return id;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getPiso() {
        return piso;
    }

    @Override
    public String toString() {
        return "Piso " + piso + " - Departamento " + departamento;
    }
}
