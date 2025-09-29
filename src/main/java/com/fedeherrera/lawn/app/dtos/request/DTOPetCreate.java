package com.fedeherrera.lawn.app.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DTOPetCreate {

    @NotEmpty
    private String nombre;
    private String raza;
    private String especie;
    private int edad;
    private String genero;
    private double peso;
    private String color;
    private String observacion;
}
