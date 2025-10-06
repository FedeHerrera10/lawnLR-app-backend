package com.fedeherrera.lawn.app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jugadores_reserva")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class JugadorReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dni;

    private Boolean esSocio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;
}
