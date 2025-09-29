package com.fedeherrera.lawn.app.controllers;

import com.fedeherrera.lawn.app.dtos.request.DisponibilidadRequest;
import com.fedeherrera.lawn.app.dtos.request.HorarioRequest;
import com.fedeherrera.lawn.app.entities.Cancha;
import com.fedeherrera.lawn.app.entities.DisponibilidadCancha;
import com.fedeherrera.lawn.app.services.cancha.CanchaService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/canchas")
@RequiredArgsConstructor
public class CanchaController {

    private final CanchaService canchaService;

    @PostMapping
    public ResponseEntity<Cancha> crearCancha(@RequestParam String nombre) {
        return ResponseEntity.ok(canchaService.crearCancha(nombre));
    }

    @PostMapping("/{id}/disponibilidad")
    public ResponseEntity<DisponibilidadCancha> crearDisponibilidad(
            @PathVariable Long id,
            @RequestBody DisponibilidadRequest request) {
        return ResponseEntity.ok(canchaService.crearDisponibilidad(id, request));
    }

    @PutMapping("/{id}/bloquear")
    public ResponseEntity<DisponibilidadCancha> bloquearHorarios(
            @PathVariable Long id,
            @RequestBody HorarioRequest request) {
        return ResponseEntity.ok(canchaService.bloquearHorarios(id, request));
    }

    @PutMapping("/{id}/ocupar")
    public ResponseEntity<DisponibilidadCancha> ocuparHorarios(
            @PathVariable Long id,
            @RequestBody HorarioRequest request) {
        return ResponseEntity.ok(canchaService.ocuparHorarios(id, request));
    }

    @GetMapping
    public ResponseEntity<List<Cancha>> obtenerTodasLasCanchas() {
        return ResponseEntity.ok(canchaService.obtenerTodasLasCanchas());
    }
    
    @PutMapping("/{id}/liberar")
    public ResponseEntity<DisponibilidadCancha> liberarHorarios(
            @PathVariable Long id,
            @RequestBody HorarioRequest request) {
        return ResponseEntity.ok(canchaService.liberarHorarios(id, request));
    }

}
