package com.aptech.finalproject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.finalproject.enums.RegistrationResult;
import com.aptech.finalproject.model.Event;
import com.aptech.finalproject.repository.EventRepository;
import com.aptech.finalproject.service.EventService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private EventService service;

    @GetMapping("")
    public List<Event> list() {
        List<Event> events = eventRepo.findAll();
        return events;
    }

    @PostMapping("")
    public Event create(@RequestBody Event event) {
        Event events = eventRepo.save(event);
        return events;
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<Map<String, String>> register(@PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        RegistrationResult result = service.register(id);

        switch (result) {
            case SUCCESS:
                response.put("message", "Registation was successful!");
                response.put("status", "success");
                return ResponseEntity.ok(response);
            case SOLD_OUT:
                response.put("message", "No Available Ticket, Event is sold out!");
                response.put("status", "error");
                return ResponseEntity.badRequest().body(response);
            case NOT_FOUND:
                response.put("message", "Event not found!");
                response.put("status", "error");
                return ResponseEntity.status(404).body(response);
            default:
                response.put("message", "Something went wrong!");
                response.put("status", "error");
                return ResponseEntity.internalServerError().body(response);
        }

    }
}
