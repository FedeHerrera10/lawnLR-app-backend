package com.fedeherrera.lawn.app.repositories;

import com.fedeherrera.lawn.app.entities.DisponibilidadCancha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DisponibilidadRepository extends JpaRepository<DisponibilidadCancha, Long> {
    Optional<DisponibilidadCancha> findByCanchaIdAndFecha(Long canchaId, LocalDate fecha);
    List<DisponibilidadCancha> findByCanchaIdAndFechaBetween(Long canchaId, LocalDate desde, LocalDate hasta);

    
}
