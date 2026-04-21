package com.flavio.appointment_booking_api.controller;

import com.flavio.appointment_booking_api.dto.appointment.AppointmentResponse;
import com.flavio.appointment_booking_api.dto.appointment.CreateAppointmentRequest;
import com.flavio.appointment_booking_api.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    public AppointmentResponse create(@RequestBody @Valid CreateAppointmentRequest request) {
        return service.create(request);
    }

    @GetMapping("/me")
    public List<AppointmentResponse> myAppointments() {
        return service.myAppointments();
    }

    @GetMapping
    public List<AppointmentResponse> all() {
        return service.all();
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id) {
        service.cancel(id);
    }
}
