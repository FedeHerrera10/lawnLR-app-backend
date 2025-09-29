package com.fedeherrera.lawn.app.controllers;


import com.fedeherrera.lawn.app.entities.DisponibilidadCancha;
import com.fedeherrera.lawn.app.services.cancha.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/canchas")
@RequiredArgsConstructor
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    /**
     * Obtiene la disponibilidad de una cancha para una fecha específica.
     *
     * Ejemplo:
     * GET /api/canchas/1/disponibilidad?fecha=2025-10-01
     */
    @GetMapping("/{canchaId}/disponibilidad")
    public ResponseEntity<DisponibilidadCancha> obtenerDisponibilidadPorFecha(
            @PathVariable Long canchaId,
            @RequestParam String fecha // formato yyyy-MM-dd
    ) {
        LocalDate f = LocalDate.parse(fecha);
        DisponibilidadCancha disponibilidad = disponibilidadService.obtenerDisponibilidadPorFecha(canchaId, f);
        return ResponseEntity.ok(disponibilidad);
    }

    /**
     * Obtiene todas las disponibilidades de una cancha dentro de un rango de fechas.
     *
     * Ejemplo:
     * GET /api/canchas/1/disponibilidades?desde=2025-10-01&hasta=2025-10-07
     */
    @GetMapping("/{canchaId}/disponibilidades")
    public ResponseEntity<List<DisponibilidadCancha>> obtenerDisponibilidadesPorRango(
            @PathVariable Long canchaId,
            @RequestParam String desde, // formato yyyy-MM-dd
            @RequestParam String hasta  // formato yyyy-MM-dd
    ) {
        LocalDate fDesde = LocalDate.parse(desde);
        LocalDate fHasta = LocalDate.parse(hasta);
        List<DisponibilidadCancha> lista = disponibilidadService.obtenerDisponibilidadesPorRango(canchaId, fDesde, fHasta);
        return ResponseEntity.ok(lista);
    }
}
