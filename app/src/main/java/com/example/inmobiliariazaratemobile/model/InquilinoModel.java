package com.example.inmobiliariazaratemobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InquilinoModel implements Serializable {

    @SerializedName(value = "idInquilino", alternate = {"inquilinoId"})
    private int idInquilino;

    @SerializedName(value = "nombre", alternate = {"nombre"})
    private String nombre;

    @SerializedName(value = "apellido", alternate = {"apellido"})
    private String apellido;


    @SerializedName("dni")
    private String dni;

    @SerializedName("lugarTrabajo")
    private String lugarTrabajo;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("email")
    private String email;

    @SerializedName(value = "direccionContacto", alternate = {"domicilio"})
    private String direccionContacto;


    public InquilinoModel() { }

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getLugarTrabajo() {
        return lugarTrabajo;
    }

    public void setLugarTrabajo(String lugarTrabajo) {
        this.lugarTrabajo = lugarTrabajo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccionContacto() {
        return direccionContacto;
    }

    public void setDireccionContacto(String direccionContacto) {
        this.direccionContacto = direccionContacto;
    }
}
