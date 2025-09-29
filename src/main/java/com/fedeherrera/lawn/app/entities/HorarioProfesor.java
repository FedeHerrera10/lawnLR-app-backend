package com.fedeherrera.lawn.app.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HorarioProfesor {
    private DayOfWeek dia;       // Ej: MONDAY, TUESDAY
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
