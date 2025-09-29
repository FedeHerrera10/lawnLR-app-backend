package com.fedeherrera.lawn.app.dtos.response;

import lombok.Data;

@Data
public class DtoPetResponse {

    private Long id;
    private String nombre;
    private String raza;
    private String especie;
    private int edad;
    private String genero;
    private double peso;
    private String color;
    private String observacion;
}