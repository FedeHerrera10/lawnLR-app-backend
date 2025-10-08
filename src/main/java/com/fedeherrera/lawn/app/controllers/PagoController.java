package com.fedeherrera.lawn.app.controllers;

import com.fedeherrera.lawn.app.services.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final MercadoPagoService mercadoPagoService;

    @PostMapping("/crear")
    public ResponseEntity<Map<String, String>> crear(@RequestBody Map<String, Object> body) {
        String descripcion = (String) body.getOrDefault("descripcion", "Reserva Cancha");
        double monto = ((Number) body.getOrDefault("monto", 0)).doubleValue();

        Map<String, String> result = mercadoPagoService.crearPreferencia(descripcion, monto);

        return ResponseEntity.ok(result); // -> devuelve { "preferenceId": "...", "init_point": "..." }
    }
}
