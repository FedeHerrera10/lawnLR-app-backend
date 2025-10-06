package com.fedeherrera.lawn.app.controllers;

import com.fedeherrera.lawn.app.dtos.request.*;
import com.fedeherrera.lawn.app.dtos.response.*;
import com.fedeherrera.lawn.app.services.reserva.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(@RequestBody ReservaRequestDTO request) {
        return ResponseEntity.ok(reservaService.crearReserva(request));
    }

    // ReservaController.java
@GetMapping("/usuario/{id}")
public List<ReservaResponseDTO> getReservasUsuario(@PathVariable Long id) {
return reservaService.getReservasUsuario(id);
}

@GetMapping
public List<ReservaResponseDTO> getAllReservas() {
    return reservaService.getAllReservas();
}

}
