package com.flavio.appointment_booking_api.service;

import com.flavio.appointment_booking_api.dto.appointment.CreateAppointmentRequest;
import com.flavio.appointment_booking_api.entity.User;
import com.flavio.appointment_booking_api.exception.BusinessException;
import com.flavio.appointment_booking_api.repository.AppointmentRepository;
import com.flavio.appointment_booking_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private User mockUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("flavio@email.com");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(mockUser.getEmail(), null)
        );

        when(userRepository.findByEmail(mockUser.getEmail()))
                .thenReturn(Optional.of(mockUser));
    }

    @Test
    void shouldCreateAppointmentSuccessfully() {

        CreateAppointmentRequest request =
                new CreateAppointmentRequest(LocalDateTime.now().plusDays(1));

        when(appointmentRepository.existsByAppointmentTime(any()))
                .thenReturn(false);
                
        when(appointmentRepository.save(any()))
        .thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> appointmentService.create(request));

        verify(appointmentRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDateIsInPast() {

        CreateAppointmentRequest request =
                new CreateAppointmentRequest(LocalDateTime.now().minusDays(1));

        assertThrows(BusinessException.class,
                () -> appointmentService.create(request));
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotIsTaken() {

        CreateAppointmentRequest request =
                new CreateAppointmentRequest(LocalDateTime.now().plusDays(1));

        when(appointmentRepository.existsByAppointmentTime(any()))
                .thenReturn(true);

        assertThrows(BusinessException.class,
                () -> appointmentService.create(request));
    }
}