package com.fedeherrera.lawn.app.services.reserva;

import com.fedeherrera.lawn.app.dtos.request.CalculoPrecioRequestDTO;
import com.fedeherrera.lawn.app.dtos.response.CalculoPrecioResponseDTO;
import com.fedeherrera.lawn.app.entities.Socios;
import com.fedeherrera.lawn.app.repositories.SocioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrecioServiceImpl implements PrecioService {

    @Autowired
    private  SocioRepository sociosRepository;

    @Override
    @Transactional(readOnly = true)
    public CalculoPrecioResponseDTO calcularPrecios(CalculoPrecioRequestDTO request) {
        List<CalculoPrecioResponseDTO.PrecioJugadorDTO> precios = request.getJugadores().stream()
        .map(jugador -> {
            // Buscar el socio por DNI
            Socios socio = sociosRepository.findByDni(jugador.getDni());
            
            // Obtener el precio por hora de la categoría del socio o 5000 si no existe
            double precioPorHora = (socio != null && socio.getCategoriaSocio() != null) ? 
                                 socio.getCategoriaSocio().getPrecio() : 5000.0;
            
            // Calcular el total para este jugador
            double total = precioPorHora * request.getCantidadHoras();
            
            // Crear y devolver el DTO del jugador con los precios
            return CalculoPrecioResponseDTO.PrecioJugadorDTO.builder()
                    .dni(jugador.getDni())
                    .precioPorHora(precioPorHora)
                    .cantidadHoras(request.getCantidadHoras())
                    .total(total)
                    .build();
        })
        .collect(Collectors.toList());
        
        // Calcular el total general
        double totalGeneral = precios.stream()
                .mapToDouble(CalculoPrecioResponseDTO.PrecioJugadorDTO::getTotal)
                .sum();
        
        // Construir y devolver la respuesta
        return CalculoPrecioResponseDTO.builder()
                .preciosPorJugador(precios)
                .totalGeneral(totalGeneral)
                .build();
    }
}
