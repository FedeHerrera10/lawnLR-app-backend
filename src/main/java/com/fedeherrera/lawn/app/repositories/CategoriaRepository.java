package com.fedeherrera.lawn.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fedeherrera.lawn.app.entities.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Categoria  findByNombre(String nombre);

}
