package com.fedeherrera.vetapp.vetapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fedeherrera.vetapp.vetapp.entities.ConfirmationToken;

import jakarta.transaction.Transactional;

@Repository("confirmationTokenRepository")
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
   Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);

  @Modifying
    @Transactional
    @Query("DELETE FROM ConfirmationToken t WHERE t.user.id = :userId")
    void deleteAllByUser_Id(@Param("userId") Long userId);
}