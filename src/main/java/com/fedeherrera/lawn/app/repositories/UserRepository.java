package com.fedeherrera.lawn.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fedeherrera.lawn.app.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    User findByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);

    User findByUsernameOrEmailIgnoreCase(String username, String email);

    
}
