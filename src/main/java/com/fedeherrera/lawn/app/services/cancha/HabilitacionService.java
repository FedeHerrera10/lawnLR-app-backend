package com.fedeherrera.lawn.app.services.cancha;


import com.fedeherrera.lawn.app.dtos.request.HabilitacionRequest;

import com.fedeherrera.lawn.app.entities.*;
import com.fedeherrera.lawn.app.repositories.CanchaRepository;
import com.fedeherrera.lawn.app.repositories.DisponibilidadRepository;
import com.fedeherrera.lawn.app.repositories.HabilitacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabilitacionService {

    private final CanchaRepository canchaRepository;
    private final HabilitacionRepository habilitacionRepository;
    private final DisponibilidadRepository disponibilidadCanchaRepository;

    public Habilitacion crearHabilitacion(HabilitacionRequest request) {
        Cancha cancha = canchaRepository.findById(request.getCanchaId())
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));

        Habilitacion habilitacion = Habilitacion.builder()
                .cancha(cancha)
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .horaInicio(request.getHoraInicio())
                .horaFin(request.getHoraFin())
                .horariosProfesores(
                        request.getHorariosProfesores() != null ?
                                request.getHorariosProfesores()
                                        .stream()
                                        .map(h -> new HorarioProfesor(h.getDia(), h.getHoraInicio(), h.getHoraFin()))
                                        .toList()
                                : null
                )
                .build();

        habilitacionRepository.save(habilitacion);

        // ⚡ Aquí generamos la disponibilidad automáticamente
        generarDisponibilidades(cancha, habilitacion);

        return habilitacion;
    }

    /**
     * Genera registros de DisponibilidadCancha para cada día entre fechaInicio y fechaFin.
     */
    private void generarDisponibilidades(Cancha cancha, Habilitacion habilitacion) {
        LocalDate start = habilitacion.getFechaInicio();
        LocalDate end = habilitacion.getFechaFin() != null ? habilitacion.getFechaFin() : start;

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {

            // --- 1. Crear todos los horarios entre horaInicio y horaFin ---
            List<String> todosLosHorarios = new ArrayList<>();
            LocalTime hora = habilitacion.getHoraInicio();
            while (!hora.isAfter(habilitacion.getHoraFin())) {
                todosLosHorarios.add(hora.toString());
                hora = hora.plusHours(1);
            }

            // --- 2. Marcar horarios ocupados por profesores según el día ---
            List<String> horariosBloqueados = new ArrayList<>();
            if (habilitacion.getHorariosProfesores() != null) {
                DayOfWeek day = date.getDayOfWeek();
                habilitacion.getHorariosProfesores().stream()
                        .filter(hp -> hp.getDia() == day)
                        .forEach(hp -> {
                            LocalTime t = hp.getHoraInicio();
                            while (!t.isAfter(hp.getHoraFin())) {
                                horariosBloqueados.add(t.toString());
                                t = t.plusHours(1);
                            }
                        });
            }

            // --- 3. El resto son horarios disponibles ---
            List<String> horariosDisponibles = todosLosHorarios.stream()
                    .filter(h -> !horariosBloqueados.contains(h))
                    .toList();

            DisponibilidadCancha disp = new DisponibilidadCancha();
            disp.setCancha(cancha);
            disp.setFecha(date);
            disp.setHorariosDisponibles(new ArrayList<>(horariosDisponibles));
            disp.setHorariosOcupados(new ArrayList<>());
            disp.setHorariosBloqueados(new ArrayList<>(horariosBloqueados));

            disponibilidadCanchaRepository.save(disp);
        }
    }
}
