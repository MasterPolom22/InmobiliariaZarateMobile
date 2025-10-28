package com.example.inmobiliariazaratemobile.model;


import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class InmuebleModel implements Serializable {

    @SerializedName("idInmueble")
    private int idInmueble;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("uso")          // "Residencial" | "Comercial"
    private String uso;

    @SerializedName("tipo")         // "Casa", "Departamento", "Local", etc.
    private String tipo;

    @SerializedName("ambientes")
    private int ambientes;

    // El backend suele usar "valor". Acepta también "precio".
    @SerializedName(value = "valor", alternate = {"precio"})
    private double precio;

    @SerializedName("disponible")
    private boolean disponible;

    // FK del dueño. Backend .NET común: "propietarioId"
    @SerializedName(value = "idPropietario", alternate = {"propietarioId"})
    private int idPropietario;

    // Datos visuales opcionales
    @SerializedName("imagen")
    private String imagen;

    // Geolocalización opcional
    @SerializedName("latitud")
    private Double latitud;   // usar wrapper para permitir null
    @SerializedName("longitud")
    private Double longitud;

    // Relación opcional
    @SerializedName(value = "duenio", alternate = {"propietario"})
    private PropietarioModel duenio;

    public InmuebleModel() {}

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

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public int getIdPropietario() { return idPropietario; }
    public void setIdPropietario(int idPropietario) { this.idPropietario = idPropietario; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public PropietarioModel getDuenio() { return duenio; }
    public void setDuenio(PropietarioModel duenio) { this.duenio = duenio; }
}