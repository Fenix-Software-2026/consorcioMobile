package com.ispc.consorciomobile.model;

public class Residente {
    private int id;
    private String username;
    private String email;
    private  String first_name;
    private  String last_name;
    private  int unidad;

    public Residente(String username, String email, String first_name, String last_name, int unidad) {
        this.username = username;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.unidad = unidad;
    }

    //getter y setters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirst_name() { return first_name; }
    public String getLast_name() { return last_name; }
    public int getUnidad() { return unidad; }

}
