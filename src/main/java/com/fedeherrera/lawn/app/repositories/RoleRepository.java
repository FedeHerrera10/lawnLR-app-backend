package com.fedeherrera.lawn.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fedeherrera.lawn.app.entities.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
    
}
    