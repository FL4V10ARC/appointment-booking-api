package com.flavio.appointment_booking_api.service;

import com.flavio.appointment_booking_api.dto.appointment.AppointmentResponse;
import com.flavio.appointment_booking_api.dto.appointment.CreateAppointmentRequest;
import com.flavio.appointment_booking_api.entity.Appointment;
import com.flavio.appointment_booking_api.entity.User;
import com.flavio.appointment_booking_api.exception.BusinessException;
import com.flavio.appointment_booking_api.repository.AppointmentRepository;
import com.flavio.appointment_booking_api.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found"));
    }

    public AppointmentResponse create(CreateAppointmentRequest request) {

        if (request.appointmentTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Cannot schedule in the past");
        }

        if (appointmentRepository.existsByAppointmentTime(request.appointmentTime())) {
            throw new BusinessException("Time slot already taken");
        }

        User client = getAuthenticatedUser();

        Appointment ap = Appointment.builder()
                .client(client)
                .appointmentTime(request.appointmentTime())
                .status("SCHEDULED")
                .createdAt(LocalDateTime.now())
                .build();

        Appointment saved = appointmentRepository.save(ap);

        return new AppointmentResponse(
                saved.getId(),
                client.getEmail(),
                saved.getAppointmentTime(),
                saved.getStatus()
        );
    }

    public List<AppointmentResponse> myAppointments() {
        User client = getAuthenticatedUser();

        return appointmentRepository.findByClient(client)
                .stream()
                .map(a -> new AppointmentResponse(
                        a.getId(),
                        client.getEmail(),
                        a.getAppointmentTime(),
                        a.getStatus()
                ))
                .toList();
    }

    public List<AppointmentResponse> all() {
        return appointmentRepository.findAll()
                .stream()
                .map(a -> new AppointmentResponse(
                        a.getId(),
                        a.getClient().getEmail(),
                        a.getAppointmentTime(),
                        a.getStatus()
                ))
                .toList();
    }

    public void cancel(Long id) {
        User user = getAuthenticatedUser();

        Appointment ap = appointmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Appointment not found"));

        boolean isOwner = ap.getClient().getId().equals(user.getId());
        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new BusinessException("Not allowed to cancel this appointment");
        }

        ap.setStatus("CANCELED");
        appointmentRepository.save(ap);
    }
}