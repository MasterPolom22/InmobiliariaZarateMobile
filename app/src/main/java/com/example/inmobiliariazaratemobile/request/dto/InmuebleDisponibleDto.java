package com.example.inmobiliariazaratemobile.request.dto;

public class InmuebleDisponibleDto {
    public int idInmueble;
    public boolean disponible;
    public InmuebleDisponibleDto(int id, boolean d){ this.idInmueble=id; this.disponible=d; }
}
