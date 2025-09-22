package com.dilain.vault.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dilain.vault.entities.RefreshToken;
import com.dilain.vault.entities.User;
import com.dilain.vault.repositories.RefreshTokenRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public String create(User user) {
        Optional<RefreshToken> foundToken = refreshTokenRepository.findByUserId(user.getId());
        System.out.println(foundToken);
        if (foundToken.isPresent()) {
            refreshTokenRepository.deleteByUser(user);
        }
        RefreshToken refreshToken = new RefreshToken();
        String token = UUID.randomUUID().toString();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
        refreshTokenRepository.save(refreshToken); 

        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void delete(Long id) {
        refreshTokenRepository.deleteById(id);
    }
    
}
