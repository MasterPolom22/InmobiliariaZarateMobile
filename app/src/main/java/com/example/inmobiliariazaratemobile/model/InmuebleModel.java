package com.example.inmobiliariazaratemobile.model;


import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class InmuebleModel implements Serializable {

    @SerializedName("idInmueble") private int idInmueble;
    @SerializedName("direccion") private String direccion;
    @SerializedName("uso") private String uso;
    @SerializedName("tipo") private String tipo;
    @SerializedName("ambientes") private int ambientes;
    @SerializedName("superficie") private double superficie;
    @SerializedName("latitud") private double latitud;
    @SerializedName("longitud") private double longitud;
    @SerializedName("valor") private double valor;
    @SerializedName("imagen") private String imagen;
    @SerializedName("disponible") private boolean disponible;

    // En muchas APIs .NET el FK llega como PropietarioId y el object como Propietario
    @SerializedName(value = "idPropietario", alternate = {"propietarioId"})
    private int idPropietario;

    @SerializedName(value = "duenio", alternate = {"propietario"})
    private PropietarioModel duenio;

    public InmuebleModel() { }

    // Getters/Setters
    public int getIdInmueble() { return idInmueble; }
    public void setIdInmueble(int idInmueble) { this.idInmueble = idInmueble; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getUso() { return uso; }
    public void setUso(String uso) { this.uso = uso; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getAmbientes() { return ambientes; }
    public void setAmbientes(int ambientes) { this.ambientes = ambientes; }
    public double getSuperficie() { return superficie; }
    public void setSuperficie(double superficie) { this.superficie = superficie; }
    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public int getIdPropietario() { return idPropietario; }
    public void setIdPropietario(int idPropietario) { this.idPropietario = idPropietario; }
    public PropietarioModel getDuenio() { return duenio; }
    public void setDuenio(PropietarioModel duenio) { this.duenio = duenio; }
}