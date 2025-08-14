package com.fedeherrera.vetapp.vetapp.dtos.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOVeterinaryInformation {
    
    private String licenseNumber;
    
    private String professionalTitle;
    
    private Set<Long> specializations;
}
