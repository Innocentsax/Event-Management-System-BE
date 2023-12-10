package dev.Innocent.EventManagementSystem.service;

import dev.Innocent.EventManagementSystem.dto.EventCategoryTicketTypesBankNames;
import dev.Innocent.EventManagementSystem.dto.EventDto;
import dev.Innocent.EventManagementSystem.dto.SearchResultDto;
import dev.Innocent.EventManagementSystem.exception.ResourceNotFoundException;
import dev.Innocent.EventManagementSystem.model.Event;
import dev.Innocent.EventManagementSystem.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface EventServices {
    ResponseEntity<List<String>> allTicketTypes();
    ResponseEntity<List<String>> allEventCategory();
    ResponseEntity<EventCategoryTicketTypesBankNames>  allTicketsAndCategoryAndBanks();
    ResponseEntity<EventDto> saveEvent(EventDto eventDto);
    ResponseEntity<String> publishEvent(Long eventId);

    EventDto editEvent(Long eventId, EventDto updateEvent);

    ResponseEntity<String> deleteEvent(Long id);
    List<Event> filterEvents(String eventTitle, String location, Date startDate, String category);
    Page<SearchResultDto> searchEvents(String eventTitle, String location, Date startDate, String category, Pageable pageable);

    ResponseEntity<EventDto> viewEvent(Long id);

    List<EventDto> getEventsCreatedByUser();
    ResponseEntity<List<Ticket>> getEventTickets(Long id) throws ResourceNotFoundException;

}
