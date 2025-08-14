package com.fedeherrera.vetapp.vetapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fedeherrera.vetapp.vetapp.entities.ImageProfile;

public interface ImageProfileRepository extends JpaRepository<ImageProfile, Long>{
    Optional<ImageProfile> findByUserId(Long userId);
}
