package com.fedeherrera.lawn.app.services.cancha;  

import com.fedeherrera.lawn.app.dtos.request.DisponibilidadRequest;
import com.fedeherrera.lawn.app.dtos.request.HorarioRequest;
import com.fedeherrera.lawn.app.entities.Cancha;
import com.fedeherrera.lawn.app.entities.DisponibilidadCancha;
import com.fedeherrera.lawn.app.repositories.CanchaRepository;
import com.fedeherrera.lawn.app.repositories.DisponibilidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CanchaService {

    private final CanchaRepository canchaRepository;
    private final DisponibilidadRepository disponibilidadRepository;

    // Crear cancha
    public Cancha crearCancha(String nombre) {
        return canchaRepository.save(Cancha.builder().nombre(nombre).build());
    }

    // Crear disponibilidad en una fecha
    public DisponibilidadCancha crearDisponibilidad(Long canchaId, DisponibilidadRequest request) {
        Cancha cancha = canchaRepository.findById(canchaId)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));

        DisponibilidadCancha disponibilidad = DisponibilidadCancha.builder()
                .cancha(cancha)
                .fecha(request.getFecha())
                .horariosDisponibles(request.getHorariosDisponibles())
                .horariosBloqueados(new ArrayList<>())
                .horariosOcupados(new ArrayList<>())
                .build();

        return disponibilidadRepository.save(disponibilidad);
    }

    // Bloquear horarios
    public DisponibilidadCancha bloquearHorarios(Long canchaId, HorarioRequest request) {
        DisponibilidadCancha disp = disponibilidadRepository
                .findByCanchaIdAndFecha(canchaId, request.getFecha())
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));

        for (String h : request.getHorarios()) {
            if (disp.getHorariosDisponibles().remove(h)) {
                disp.getHorariosBloqueados().add(h);
            }
        }

        return disponibilidadRepository.save(disp);
    }

    // Ocupar horarios
    public DisponibilidadCancha ocuparHorarios(Long canchaId, HorarioRequest request) {
        DisponibilidadCancha disp = disponibilidadRepository
                .findByCanchaIdAndFecha(canchaId, request.getFecha())
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));

        for (String h : request.getHorarios()) {
            if (disp.getHorariosDisponibles().remove(h)) {
                disp.getHorariosOcupados().add(h);
            }
        }

        return disponibilidadRepository.save(disp);
    }

    public List<Cancha> obtenerTodasLasCanchas() {
        return canchaRepository.findAll();
    }

    public DisponibilidadCancha liberarHorarios(Long canchaId, HorarioRequest request) {
        DisponibilidadCancha disp = disponibilidadRepository
                .findByCanchaIdAndFecha(canchaId, request.getFecha())
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
    
        for (String h : request.getHorarios()) {
            String horario = h.trim();
    
            // Si estaba bloqueado
            if (disp.getHorariosBloqueados().remove(horario)) {
                if (!disp.getHorariosDisponibles().contains(horario)) {
                    disp.getHorariosDisponibles().add(horario);
                }
            }
            // Si estaba ocupado
            else if (disp.getHorariosOcupados().remove(horario)) {
                if (!disp.getHorariosDisponibles().contains(horario)) {
                    disp.getHorariosDisponibles().add(horario);
                }
            }
        }
    
        return disponibilidadRepository.save(disp);
    }
    
    
}




