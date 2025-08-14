package com.fedeherrera.vetapp.vetapp.repositories;

import com.fedeherrera.vetapp.vetapp.entities.User;
import com.fedeherrera.vetapp.vetapp.entities.VeterinaryInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeterinaryInformationRepository extends JpaRepository<VeterinaryInformation, Long> {
    // Buscar por usuario
    Optional<VeterinaryInformation> findByUser(User user);
}
