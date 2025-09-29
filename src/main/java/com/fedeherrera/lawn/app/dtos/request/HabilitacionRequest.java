package com.fedeherrera.lawn.app.dtos.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class HabilitacionRequest {
    private Long canchaId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin; // opcional
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private List<HorarioProfesorDTO> horariosProfesores;
}
