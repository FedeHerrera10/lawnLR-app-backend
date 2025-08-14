package com.fedeherrera.vetapp.vetapp.repositories;

import com.fedeherrera.vetapp.vetapp.entities.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    
    // Buscar especialización por nombre (ignorando mayúsculas/minúsculas)
    Optional<Specialization> findByNameIgnoreCase(String name);
    
    // Verificar si existe una especialización con un nombre específico (ignorando mayúsculas/minúsculas)
    boolean existsByNameIgnoreCase(String name);
    
    // Buscar especializaciones por nombre que contenga el texto (ignorando mayúsculas/minúsculas)
    List<Specialization> findByNameContainingIgnoreCase(String name);
    
    // Buscar especializaciones por ID en una lista
    List<Specialization> findByIdIn(List<Long> ids);
}
