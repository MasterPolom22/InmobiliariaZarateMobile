package com.example.inmobiliariazaratemobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GaranteModel implements Serializable {

    @SerializedName("nombreGarante")
    private String nombreGarante;

    @SerializedName("dniGarante")
    private String dniGarante;

    @SerializedName("telefonoGarante")
    private String telefonoGarante;

    @SerializedName("emailGarante")
    private String emailGarante;
}
