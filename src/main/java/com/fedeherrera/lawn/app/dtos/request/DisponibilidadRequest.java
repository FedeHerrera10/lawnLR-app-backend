package com.fedeherrera.lawn.app.dtos.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class DisponibilidadRequest {
    private LocalDate fecha;
    private List<String> horariosDisponibles;
}

