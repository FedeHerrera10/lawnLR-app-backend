package com.fedeherrera.vetapp.vetapp.dtos.response;

import java.util.List;

import lombok.Data;

@Data
public class DTOVeterinaryInfo {
    private String licenseNumber;
    private String professionalTitle;
    private List<DTOSpecialization> specializations;  // Lista de nombres de especializaciones
}
