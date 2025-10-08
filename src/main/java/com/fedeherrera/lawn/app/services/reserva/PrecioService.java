package com.fedeherrera.lawn.app.services.reserva;

import com.fedeherrera.lawn.app.dtos.request.CalculoPrecioRequestDTO;
import com.fedeherrera.lawn.app.dtos.response.CalculoPrecioResponseDTO;

public interface PrecioService {
    CalculoPrecioResponseDTO calcularPrecios(CalculoPrecioRequestDTO request);
}
