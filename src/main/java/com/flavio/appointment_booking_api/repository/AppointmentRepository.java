package com.flavio.appointment_booking_api.repository;

import com.flavio.appointment_booking_api.entity.Appointment;
import com.flavio.appointment_booking_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByClient(User client);

    boolean existsByAppointmentTime(LocalDateTime appointmentTime);
}