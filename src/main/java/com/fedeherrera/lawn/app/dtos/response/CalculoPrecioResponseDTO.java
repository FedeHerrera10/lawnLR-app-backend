package com.fedeherrera.lawn.app.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculoPrecioResponseDTO {
    private List<PrecioJugadorDTO> preciosPorJugador;
    private double totalGeneral;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrecioJugadorDTO {
        private String dni;
        private double precioPorHora;
        private int cantidadHoras;
        private double total;
    }
}
