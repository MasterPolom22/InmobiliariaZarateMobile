package com.example.inmobiliariazaratemobile.model;

import java.io.Serializable;
public class InmuebleModel implements Serializable{


    private int idInmueble;
    private String direccion;
    private String uso;
    private String tipo;
    private int ambientes;
    private double superficie;
    private double latitud;
    private double longitud;
    private double valor;
    private String imagen;
    private boolean disponible;
    private int idPropietario;
    private PropietarioModel duenio;

    public InmuebleModel(int idInmueble, PropietarioModel duenio, int idPropietario, boolean disponible, String imagen, double valor, double longitud, double latitud, double superficie, int ambientes, String tipo, String uso, String direccion) {
        this.idInmueble = idInmueble;
        this.duenio = duenio;
        this.idPropietario = idPropietario;
        this.disponible = disponible;
        this.imagen = imagen;
        this.valor = valor;
        this.longitud = longitud;
        this.latitud = latitud;
        this.superficie = superficie;
        this.ambientes = ambientes;
        this.tipo = tipo;
        this.uso = uso;
        this.direccion = direccion;
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public PropietarioModel getDuenio() {
        return duenio;
    }

    public void setDuenio(PropietarioModel duenio) {
        this.duenio = duenio;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public int getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(int ambientes) {
        this.ambientes = ambientes;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
