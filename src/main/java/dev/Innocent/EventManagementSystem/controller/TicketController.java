package dev.Innocent.EventManagementSystem.controller;


import dev.Innocent.EventManagementSystem.repository.EventRepository;
import dev.Innocent.EventManagementSystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final EventRepository eventRepository;

    @Autowired
    public TicketController(TicketService ticketService, EventRepository eventRepository) {
        this.ticketService = ticketService;
        this.eventRepository= eventRepository;
    }

    @GetMapping("/eventTicket/{eventId}")
    public ResponseEntity<Map<String, Map<String, Integer>>> FindAllByEventId(@PathVariable Long eventId) {
        Map<String, Map<String, Integer>> ticketInformation = ticketService.findAllByEventId(eventId);
        return ResponseEntity.ok(ticketInformation);
    }
}

