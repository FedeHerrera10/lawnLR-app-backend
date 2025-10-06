package com.fedeherrera.lawn.app.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import com.fedeherrera.lawn.app.dtos.JugadorDTO;

@Data
@Builder
public class ReservaResponseDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private Long canchaId;
    private String canchaNombre;
    private LocalDate fecha;
    private List<String> horarios;
    private List<JugadorDTO> jugadores;
    private Double montoTotal;
    private String estado;
}
