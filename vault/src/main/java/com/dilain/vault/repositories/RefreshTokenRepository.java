package com.dilain.vault.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.dilain.vault.entities.RefreshToken;
import com.dilain.vault.entities.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(Long id);
    void deleteByUser(User user);
}
