package com.fedeherrera.lawn.app.controllers;

import com.fedeherrera.lawn.app.dtos.request.HabilitacionRequest;
import com.fedeherrera.lawn.app.entities.Habilitacion;
import com.fedeherrera.lawn.app.services.cancha.HabilitacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/habilitaciones")
@RequiredArgsConstructor
public class HabilitacionController {

    private final HabilitacionService habilitacionService;

    @PostMapping
    public ResponseEntity<Habilitacion> crear(@RequestBody HabilitacionRequest request) {
        return ResponseEntity.ok(habilitacionService.crearHabilitacion(request));
    }
}
