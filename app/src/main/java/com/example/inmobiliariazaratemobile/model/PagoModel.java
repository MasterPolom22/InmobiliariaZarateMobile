package com.example.inmobiliariazaratemobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PagoModel implements Serializable {
    @SerializedName(value = "idPago", alternate = {"pagoId"})
    private int idPago;

    @SerializedName(value = "idContrato", alternate = {"contratoId"})
    private int idContrato;

    @SerializedName("numeroPago")
    private int numeroPago;

    @SerializedName("fechaPago")
    private String fechaPago;

    @SerializedName("importe")
    private double importe;

    // Opcionales
    @SerializedName("periodoMes")
    private Integer periodoMes;

    @SerializedName("periodoAnio")
    private Integer periodoAnio;

    @SerializedName("medioPago")
    private String medioPago;

    @SerializedName("estado")
    private String estado;

    @SerializedName("recargo")
    private Double recargo;

    @SerializedName("descuento")
    private Double descuento;

    @SerializedName("observaciones")
    private String observaciones;

    @SerializedName("comprobanteUrl")
    private String comprobanteUrl;

    public PagoModel() {
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public int getNumeroPago() {
        return numeroPago;
    }

    public void setNumeroPago(int numeroPago) {
        this.numeroPago = numeroPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public Integer getPeriodoMes() {
        return periodoMes;
    }

    public void setPeriodoMes(Integer periodoMes) {
        this.periodoMes = periodoMes;
    }

    public Integer getPeriodoAnio() {
        return periodoAnio;
    }

    public void setPeriodoAnio(Integer periodoAnio) {
        this.periodoAnio = periodoAnio;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getRecargo() {
        return recargo;
    }

    public void setRecargo(Double recargo) {
        this.recargo = recargo;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getComprobanteUrl() {
        return comprobanteUrl;
    }

    public void setComprobanteUrl(String comprobanteUrl) {
        this.comprobanteUrl = comprobanteUrl;
    }
}
