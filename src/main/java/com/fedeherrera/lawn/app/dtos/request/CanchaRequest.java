package com.fedeherrera.lawn.app.dtos.request;


import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CanchaRequest {
    private String nombre;
    private LocalDate fecha;
    private List<String> horariosDisponibles;
}
