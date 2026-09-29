package com.workintech.twitterclone.service;

import com.workintech.twitterclone.dto.RegisterRequest;
import com.workintech.twitterclone.dto.UserResponse;
import com.workintech.twitterclone.entity.User;
import com.workintech.twitterclone.exception.EmailAlreadyExistsException;
import com.workintech.twitterclone.exception.UsernameAlreadyExistsException;
import com.workintech.twitterclone.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_kullaniciAdiZatenVarsaExceptionFirlatir() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("sinem");
        request.setEmail("sinem@example.com");
        request.setPassword("guclusifre1");

        when(userRepository.existsByUsername("sinem")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(UsernameAlreadyExistsException.class);
    }

    @Test
    void register_emailZatenVarsaExceptionFirlatir() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("yeniKullanici");
        request.setEmail("sinem@example.com");
        request.setPassword("guclusifre1");

        when(userRepository.existsByUsername("yeniKullanici")).thenReturn(false);
        when(userRepository.existsByEmail("sinem@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void register_sifreyiHashleyerekKaydeder() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("yeniKullanici");
        request.setEmail("yeni@example.com");
        request.setPassword("guclusifre1");

        when(userRepository.existsByUsername("yeniKullanici")).thenReturn(false);
        when(userRepository.existsByEmail("yeni@example.com")).thenReturn(false);
        when(passwordEncoder.encode("guclusifre1")).thenReturn("hashed-value");

        User savedUser = new User();
        savedUser.setId(5L);
        savedUser.setUsername("yeniKullanici");
        savedUser.setEmail("yeni@example.com");
        savedUser.setPassword("hashed-value");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.register(request);

        assertThat(response.getUsername()).isEqualTo("yeniKullanici");
    }
}
