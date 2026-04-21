package com.flavio.appointment_booking_api.dto.appointment;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotNull(message = "appointmentTime is required")
        LocalDateTime appointmentTime
) {}