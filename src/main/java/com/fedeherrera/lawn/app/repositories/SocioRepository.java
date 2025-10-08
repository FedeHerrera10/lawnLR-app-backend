package com.fedeherrera.lawn.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fedeherrera.lawn.app.entities.Socios;


public interface SocioRepository extends JpaRepository<Socios, Long> {
    Socios  findByDni(String dni);
}
