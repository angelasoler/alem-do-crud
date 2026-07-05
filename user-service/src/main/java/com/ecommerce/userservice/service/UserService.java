package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.UserRegistrationRequest;
import com.ecommerce.userservice.exception.UserAlreadyExistsException;
import com.ecommerce.userservice.model.User;
import com.ecommerce.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationRequest request) {

        userRepository.findByDocumentNumber(request.getDocumentNumber()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with this document number already exists.");
        });

        userRepository.findByLogin(request.getLogin()).ifPresent(u -> {
            throw new UserAlreadyExistsException("User with this login already exists.");
        });

        User user = new User();
        user.setFullName(request.getFullName());
        user.setDocumentNumber(request.getDocumentNumber());
        user.setLogin(request.getLogin());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
