package com.example.inmobiliariazaratemobile.request.dto;

import com.google.gson.annotations.SerializedName;

public class InmuebleCreateDto {
    @SerializedName("direccion")   public String direccion;
    @SerializedName("usoId")       public int usoId;   // ENTERO
    @SerializedName("tipoId")      public int tipoId;  // ENTERO
    @SerializedName("ambientes")   public int ambientes;
    @SerializedName("superficie")  public double superficie;
    @SerializedName("latitud")     public double latitud;
    @SerializedName("longitud")    public double longitud;
    @SerializedName("valor")       public double valor;
    @SerializedName("disponible")  public boolean disponible;
}
