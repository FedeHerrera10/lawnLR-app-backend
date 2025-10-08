package com.fedeherrera.lawn.app.dtos.request;

import lombok.Data;
import java.util.List;

@Data
public class CalculoPrecioRequestDTO {
    private List<JugadorPrecioDTO> jugadores;
    private int cantidadHoras;
    
    @Data
    public static class JugadorPrecioDTO {
        private String dni;
    }
}
