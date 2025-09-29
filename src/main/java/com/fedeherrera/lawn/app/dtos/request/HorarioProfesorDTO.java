package com.fedeherrera.lawn.app.dtos.request;


import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class HorarioProfesorDTO {
    private DayOfWeek dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
