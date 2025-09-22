package com.dilain.vault.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dilain.vault.dtos.auth.RegisterRequest;
import com.dilain.vault.dtos.user.UserDto;
import com.dilain.vault.entities.User;
import com.dilain.vault.exceptions.UsernameAlreadyTakenException;
import com.dilain.vault.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDto createUser(RegisterRequest request) {
        if(userRepository.findByUsername(request.username()).isPresent()) {
            throw new UsernameAlreadyTakenException("username already taken");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        User createdUser = userRepository.save(user);
        return UserDto.from(createdUser);
    }
}
