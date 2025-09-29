package com.fedeherrera.lawn.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fedeherrera.lawn.app.entities.ImageProfile;

public interface ImageProfileRepository extends JpaRepository<ImageProfile, Long>{
    Optional<ImageProfile> findByUserId(Long userId);
}
