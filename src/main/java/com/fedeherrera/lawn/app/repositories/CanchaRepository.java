package com.fedeherrera.lawn.app.repositories;

import com.fedeherrera.lawn.app.entities.Cancha;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CanchaRepository extends JpaRepository<Cancha, Long> {
    @Query("SELECT DISTINCT c FROM Cancha c " +
           "JOIN FETCH c.disponibilidades d " +
           "WHERE d.fecha = :fecha")
    List<Cancha> findCanchasConDisponibilidadesPorFecha(@Param("fecha") LocalDate fecha);
}
