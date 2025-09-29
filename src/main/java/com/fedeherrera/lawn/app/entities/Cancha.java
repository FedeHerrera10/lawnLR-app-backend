package com.fedeherrera.lawn.app.entities; 

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // Ej: "Cancha 1"

// Cancha.java
@OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
@JsonManagedReference
private List<DisponibilidadCancha> disponibilidades;


    // private LocalDate fecha;

    // @ElementCollection
    // private List<String> horariosDisponibles;

    // @ElementCollection
    // private List<String> horariosBloqueados;

    // @ElementCollection
    // private List<String> horariosOcupados;
}
