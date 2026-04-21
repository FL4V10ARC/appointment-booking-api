package com.flavio.appointment_booking_api.controller;

import com.flavio.appointment_booking_api.dto.appointment.AppointmentResponse;
import com.flavio.appointment_booking_api.dto.appointment.CreateAppointmentRequest;
import com.flavio.appointment_booking_api.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@Tag(name = "Appointments", description = "Appointment management endpoints")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new appointment")
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody @Valid CreateAppointmentRequest request) {
        AppointmentResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List authenticated user's appointments")
    @GetMapping("/me")
    public ResponseEntity<List<AppointmentResponse>> myAppointments() {
        return ResponseEntity.ok(service.myAppointments());
    }

    @Operation(summary = "List all appointments (ADMIN only)")
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> all() {
        return ResponseEntity.ok(service.all());
    }

    @Operation(summary = "Cancel an appointment")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        service.cancel(id);
        return ResponseEntity.noContent().build();
    }
}