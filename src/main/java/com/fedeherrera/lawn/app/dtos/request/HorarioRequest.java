package com.fedeherrera.lawn.app.dtos.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class HorarioRequest {
    private LocalDate fecha;
    private List<String> horarios; // Ejemplo: ["11:00", "12:00"]
}

