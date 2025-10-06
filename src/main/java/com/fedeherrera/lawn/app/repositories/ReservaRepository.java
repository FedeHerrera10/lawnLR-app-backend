package com.fedeherrera.lawn.app.repositories;

import com.fedeherrera.lawn.app.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByCanchaIdAndFecha(Long canchaId, LocalDate fecha);
    List<Reserva> findByUsuarioId(Long usuarioId);

}
