package dev.Innocent.EventManagementSystem.controller;

import dev.Innocent.EventManagementSystem.dto.EventCategoryTicketTypesBankNames;
import dev.Innocent.EventManagementSystem.dto.EventDto;
import dev.Innocent.EventManagementSystem.dto.SearchResultDto;
import dev.Innocent.EventManagementSystem.exception.ResourceNotFoundException;
import dev.Innocent.EventManagementSystem.model.Ticket;
import dev.Innocent.EventManagementSystem.service.EventServices;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class EventController {
    private final EventServices eventServices;

    @GetMapping("ticket_types")
    public ResponseEntity<List<String>> allTicketTypes() {
        return eventServices.allTicketTypes();
    }

    @GetMapping("all_event_category")
    public ResponseEntity<List<String>> allEventCategory() {
        return eventServices.allEventCategory();
    }

    @GetMapping("all_tickets_category_banks")
    public ResponseEntity<EventCategoryTicketTypesBankNames> allTicketsAndCategoryAndBanks() {
        return eventServices.allTicketsAndCategoryAndBanks();
    }

    @PostMapping("save_event")
    public ResponseEntity<EventDto> saveEvent(@RequestBody EventDto eventDto) {
        return eventServices.saveEvent(eventDto);
    }

    @PutMapping("/publish/{eventId}")
    public ResponseEntity<String> publishEvent(@PathVariable Long eventId) {
        return eventServices.publishEvent(eventId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        return eventServices.deleteEvent(id);
    }


    @GetMapping("/search")
    public ResponseEntity<Page<SearchResultDto>> searchEvents(
            @RequestParam(required = false) String eventTitle,
            @RequestParam(required = false) String location,
            @RequestParam(required = false, defaultValue = "All Category") String category,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int pageSize) {
        int maxSize = 20;
        pageSize = Math.min(pageSize, maxSize);

        Date convertedStartDate = parseStartDate(startDate);

        Page<SearchResultDto> events = eventServices.searchEvents(eventTitle, location, convertedStartDate, category,
                PageRequest.of(page, pageSize));
        return ResponseEntity.ok(events);
    }

    private Date parseStartDate(String startDateString) {
        if (startDateString != null && !startDateString.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(startDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GetMapping("/view/{eventId}")
    public ResponseEntity<EventDto> viewEvent(@PathVariable Long eventId) {
        EventDto eventDto = eventServices.viewEvent(eventId).getBody();
        if (eventDto != null) {
            return ResponseEntity.ok(eventDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/UserEvent")
    public List<EventDto> getEventsCreatedByUser() {
        return eventServices.getEventsCreatedByUser();
    }


    @PutMapping("/edit/{eventId}")
    public ResponseEntity<EventDto> editEvent(@PathVariable Long eventId, @RequestBody EventDto updateEvent) {
        try {
            return new ResponseEntity<>(eventServices.editEvent(eventId, updateEvent), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event Not Found", e);
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied", e);
        }
    }

    @GetMapping("get_event_ticket/{id}")
    public ResponseEntity<List<Ticket>> getEventTickets(@PathVariable Long id) throws ResourceNotFoundException {
        return eventServices.getEventTickets(id);
    }

}
