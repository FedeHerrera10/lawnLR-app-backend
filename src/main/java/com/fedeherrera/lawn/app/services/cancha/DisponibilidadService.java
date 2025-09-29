package com.fedeherrera.lawn.app.services.cancha;

import com.fedeherrera.lawn.app.entities.DisponibilidadCancha;
import com.fedeherrera.lawn.app.repositories.DisponibilidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilidadService {

    private final DisponibilidadRepository disponibilidadCanchaRepository;

    /**
     * Obtiene la disponibilidad de una cancha para una fecha exacta.
     *
     * @param canchaId ID de la cancha
     * @param fecha    Fecha que se quiere consultar (yyyy-MM-dd)
     * @return DisponibilidadCancha para la fecha indicada
     */
    public DisponibilidadCancha obtenerDisponibilidadPorFecha(Long canchaId, LocalDate fecha) {
        return disponibilidadCanchaRepository.findByCanchaIdAndFecha(canchaId, fecha)
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró disponibilidad para la cancha " + canchaId + " en la fecha " + fecha));
    }

    /**
     * Obtiene todas las disponibilidades de una cancha dentro de un rango de fechas.
     *
     * @param canchaId ID de la cancha
     * @param desde    Fecha de inicio (inclusive)
     * @param hasta    Fecha de fin (inclusive)
     * @return Lista de disponibilidades dentro del rango
     */
    public List<DisponibilidadCancha> obtenerDisponibilidadesPorRango(Long canchaId, LocalDate desde, LocalDate hasta) {
        return disponibilidadCanchaRepository.findByCanchaIdAndFechaBetween(canchaId, desde, hasta);
    }
}
