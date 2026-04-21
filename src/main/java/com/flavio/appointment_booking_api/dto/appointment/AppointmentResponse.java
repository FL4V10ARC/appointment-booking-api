package com.flavio.appointment_booking_api.dto.appointment;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        String clientEmail,
        LocalDateTime appointmentTime,
        String status
) {}