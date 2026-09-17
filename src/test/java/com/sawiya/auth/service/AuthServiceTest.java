package com.sawiya.auth.service;

import com.sawiya.auth.dto.SigninRequestDTO;
import com.sawiya.auth.dto.SigninResponseDTO;
import com.sawiya.auth.dto.SignupRequestDTO;
import com.sawiya.auth.entity.User;
import com.sawiya.auth.exception.DuplicateEmailException;
import com.sawiya.auth.exception.InvalidCredentialsException;
import com.sawiya.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        authService = new AuthService(
                userRepository,
                passwordEncoder
        );
    }

    @Test
    void shouldSigninSuccessfullyWithValidCredentials() {

        SigninRequestDTO request = new SigninRequestDTO();
        request.setEmail("john.doe@example.com");
        request.setPassword("Password@123");

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("$2a$10$hashedPassword")
                .build();

        when(userRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Password@123",
                "$2a$10$hashedPassword"
        )).thenReturn(true);

        SigninResponseDTO response = authService.signin(request);

        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    void shouldRejectSigninWithWrongPassword() {

        SigninRequestDTO request = new SigninRequestDTO();
        request.setEmail("john.doe@example.com");
        request.setPassword("WrongPassword@123");

        User user = User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .password("$2a$10$hashedPassword")
                .build();

        when(userRepository.findByEmail("john.doe@example.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "WrongPassword@123",
                "$2a$10$hashedPassword"
        )).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.signin(request)
        );
    }

    @Test
    void shouldRejectSigninWhenUserDoesNotExist() {

        SigninRequestDTO request = new SigninRequestDTO();
        request.setEmail("unknown@example.com");
        request.setPassword("Password@123");

        when(userRepository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.signin(request)
        );
    }

    @Test
    void shouldRejectSignupWhenEmailAlreadyExists() {

        SignupRequestDTO request = new SignupRequestDTO();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("Password@123");

        when(userRepository.existsByEmail("john.doe@example.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateEmailException.class,
                () -> authService.signup(request)
        );
    }

}
