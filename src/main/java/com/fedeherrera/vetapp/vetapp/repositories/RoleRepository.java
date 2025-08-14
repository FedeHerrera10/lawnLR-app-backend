package com.fedeherrera.vetapp.vetapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fedeherrera.vetapp.vetapp.entities.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
    
}
    