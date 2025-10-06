package com.fedeherrera.lawn.app.dtos.request;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

import com.fedeherrera.lawn.app.dtos.JugadorDTO;

@Data
public class ReservaRequestDTO {
    private Long usuarioId;
    private Long canchaId;
    private LocalDate fecha;
    private List<String> horarios;
    private List<JugadorDTO> jugadores;
    private Double montoTotal;
    private String idPago;
}
