package com.fedeherrera.lawn.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisponibilidadCancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    @ElementCollection
    private List<String> horariosDisponibles;

    @ElementCollection
    private List<String> horariosBloqueados;

    @ElementCollection
    private List<String> horariosOcupados;

// DisponibilidadCancha.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "cancha_id")
@JsonBackReference
private Cancha cancha;

}
