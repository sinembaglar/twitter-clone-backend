package com.workintech.twitterclone.service;

import com.workintech.twitterclone.dto.RegisterRequest;
import com.workintech.twitterclone.dto.UserResponse;
import com.workintech.twitterclone.entity.User;
import com.workintech.twitterclone.exception.EmailAlreadyExistsException;
import com.workintech.twitterclone.exception.UsernameAlreadyExistsException;
import com.workintech.twitterclone.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Bu kullanici adi zaten kullaniliyor: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Bu email zaten kullaniliyor: " + request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }
}
