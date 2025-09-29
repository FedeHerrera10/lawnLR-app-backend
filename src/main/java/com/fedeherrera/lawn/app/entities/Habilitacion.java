package com.fedeherrera.lawn.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Habilitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---- Cancha ----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancha_id")
    private Cancha cancha;

    // ---- Vigencia ----
    private LocalDate fechaInicio;
    private LocalDate fechaFin; // puede ser null => sin fin

    // ---- Horario general de habilitación ----
    private LocalTime horaInicio;
    private LocalTime horaFin;

    // ---- Horarios de profesores ----
    @ElementCollection
    @CollectionTable(name = "habilitacion_profesores", joinColumns = @JoinColumn(name = "habilitacion_id"))
    private List<HorarioProfesor> horariosProfesores;
}
