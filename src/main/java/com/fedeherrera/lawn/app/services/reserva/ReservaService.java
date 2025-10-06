package com.fedeherrera.lawn.app.services.reserva;

import com.fedeherrera.lawn.app.dtos.JugadorDTO;
import com.fedeherrera.lawn.app.dtos.request.ReservaRequestDTO;
import com.fedeherrera.lawn.app.dtos.response.ReservaResponseDTO;
import com.fedeherrera.lawn.app.entities.*;
import com.fedeherrera.lawn.app.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UserRepository userRepository;
    private final CanchaRepository canchaRepository;

    public ReservaResponseDTO crearReserva(ReservaRequestDTO request) {
        User usuario = userRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cancha cancha = canchaRepository.findById(request.getCanchaId())
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));

        // ✅ Validar que los horarios no estén ocupados
        List<Reserva> reservasExistentes =
                reservaRepository.findByCanchaIdAndFecha(cancha.getId(), request.getFecha());

        for (Reserva r : reservasExistentes) {
            if (r.getHorarios() != null) {
                for (String h : r.getHorarios()) {
                    if (request.getHorarios().contains(h)) {
                        throw new RuntimeException("El horario " + h + " ya está reservado");
                    }
                }
            }
        }

        // ✅ Crear reserva
        Reserva reserva = Reserva.builder()
                .usuario(usuario)
                .cancha(cancha)
                .fecha(request.getFecha())
                .horarios(request.getHorarios())
                .montoTotal(request.getMontoTotal())
                .idPago(request.getIdPago())
                .estado(EstadoReserva.PENDIENTE)
                .build();

        // ✅ Crear jugadores asociados
        List<JugadorReserva> jugadores = request.getJugadores().stream()
                .map(j -> JugadorReserva.builder()
                        .dni(j.getDni())
                        .esSocio(validarSocio(j.getDni()))
                        .reserva(reserva)
                        .build())
                .collect(Collectors.toList());

        reserva.setJugadores(jugadores);

        // ✅ Guardar en base de datos
        Reserva saved = reservaRepository.save(reserva);

        // ⚡ Forzar inicialización de la lista de horarios para evitar null
        if (saved.getHorarios() != null) {
            saved.getHorarios().size();
        }

        // ✅ Construir la respuesta con la reserva guardada
        return ReservaResponseDTO.builder()
                .id(saved.getId())
                .usuarioId(saved.getUsuario().getId())
                .usuarioNombre(saved.getUsuario().getUsername())
                .canchaId(saved.getCancha().getId())
                .canchaNombre(saved.getCancha().getNombre())
                .fecha(saved.getFecha())
                .horarios(saved.getHorarios())
                .jugadores(saved.getJugadores().stream()
                        .map(j -> new JugadorDTO(j.getDni(), j.getEsSocio())) // 🔹 <--- CAMBIO AQUÍ
                        .collect(Collectors.toList()))
                .montoTotal(saved.getMontoTotal())
                .estado(saved.getEstado().name())
                .build();
    }

    private boolean validarSocio(String dni) {
        // TODO: implementar lógica real para verificar si el DNI corresponde a un socio
        return false;
    }

    public List<ReservaResponseDTO> getReservasUsuario(Long userId) {
        List<Reserva> reservas = reservaRepository.findByUsuarioId(userId);
        return reservas.stream().map(r -> ReservaResponseDTO.builder()
                .id(r.getId())
                .usuarioId(r.getUsuario().getId())
                .usuarioNombre(r.getUsuario().getUsername())
                .canchaId(r.getCancha().getId())
                .canchaNombre(r.getCancha().getNombre())
                .fecha(r.getFecha())
                .horarios(r.getHorarios())
                .jugadores(r.getJugadores().stream()
                        .map(j -> new JugadorDTO(j.getDni(), j.getEsSocio()))
                        .collect(Collectors.toList()))
                .montoTotal(r.getMontoTotal())
                .estado(r.getEstado().name())
                .build()
        ).toList();
    }

    public List<ReservaResponseDTO> getAllReservas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return reservas.stream()
                .map(r -> ReservaResponseDTO.builder()
                        .id(r.getId())
                        .usuarioId(r.getUsuario().getId())
                        .usuarioNombre(r.getUsuario().getUsername())
                        .canchaId(r.getCancha().getId())
                        .canchaNombre(r.getCancha().getNombre())
                        .fecha(r.getFecha())
                        .horarios(r.getHorarios())
                        .jugadores(r.getJugadores().stream()
                                .map(j -> new JugadorDTO(j.getDni(), j.getEsSocio()))
                                .collect(Collectors.toList()))
                        .montoTotal(r.getMontoTotal())
                        .estado(r.getEstado().name())
                        .build()
                )
                .collect(Collectors.toList());
    }
    
}
