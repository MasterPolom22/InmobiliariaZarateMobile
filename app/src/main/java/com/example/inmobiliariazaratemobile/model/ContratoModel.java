package com.example.inmobiliariazaratemobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ContratoModel implements Serializable {
    @SerializedName(value = "idContrato", alternate = {"contratoId"})
    private int idContrato;

    // Fechas en ISO-8601 (ej: "2025-10-28"). Usar String evita adapters extra.
    @SerializedName("fechaInicio")
    private String fechaInicio;

    @SerializedName("fechaFin")
    private String fechaFin;

    @SerializedName(value = "montoAlquiler", alternate = {"monto"})
    private double montoAlquiler;

    // FK
    @SerializedName(value = "idInmueble", alternate = {"inmuebleId"})
    private int idInmueble;

    @SerializedName(value = "idInquilino", alternate = {"inquilinoId"})
    private int idInquilino;

    // "VIGENTE" | "FINALIZADO" | "RESCINDIDO"
    @SerializedName("estado")
    private String estado;

    // Opcionales para rescisión/renovación
    @SerializedName("fechaRescision")
    private String fechaRescision;         // null si no aplica

    @SerializedName("multaRescision")
    private Double multaRescision;         // null si no aplica

    @SerializedName(value = "contratoOrigenId", alternate = {"renuevaDeContratoId"})
    private Integer contratoOrigenId;      // null si no es renovación

    // Relaciones opcionales (si el backend las incluye embebidas)
    @SerializedName("inmueble")
    private InmuebleModel inmueble;        // puede venir null

    @SerializedName("inquilino")
    private InquilinoModel inquilino;      // puede venir null

    public ContratoModel() {}


    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getMontoAlquiler() {
        return montoAlquiler;
    }

    public void setMontoAlquiler(double montoAlquiler) {
        this.montoAlquiler = montoAlquiler;
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaRescision() {
        return fechaRescision;
    }

    public void setFechaRescision(String fechaRescision) {
        this.fechaRescision = fechaRescision;
    }

    public Double getMultaRescision() {
        return multaRescision;
    }

    public void setMultaRescision(Double multaRescision) {
        this.multaRescision = multaRescision;
    }

    public Integer getContratoOrigenId() {
        return contratoOrigenId;
    }

    public void setContratoOrigenId(Integer contratoOrigenId) {
        this.contratoOrigenId = contratoOrigenId;
    }

    public InmuebleModel getInmueble() {
        return inmueble;
    }

    public void setInmueble(InmuebleModel inmueble) {
        this.inmueble = inmueble;
    }

    public InquilinoModel getInquilino() {
        return inquilino;
    }

    public void setInquilino(InquilinoModel inquilino) {
        this.inquilino = inquilino;
    }
}
