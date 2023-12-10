package dev.Innocent.EventManagementSystem.serviceImpl;



import dev.Innocent.EventManagementSystem.model.Event;
import dev.Innocent.EventManagementSystem.model.Ticket;
import dev.Innocent.EventManagementSystem.repository.EventRepository;
import dev.Innocent.EventManagementSystem.repository.TicketRepository;
import dev.Innocent.EventManagementSystem.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;



    @Override
    public Map<String, Map<String, Integer>> findAllByEventId(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isPresent()) {
            Event targetEvent = event.get();

            // Assuming each event has a list of tickets associated with it
            List<Ticket> eventTickets = targetEvent.getTickets();

            // Create a map to store quantity sold and quantity left as lists for each ticket category
            Map<String, Map<String, Integer>> ticketInformation = new HashMap<>();

            // Calculate both quantity sold and quantity left for each ticket category
            for (Ticket ticket : eventTickets) {
                String ticketCategory = ticket.getTicket(); // Assuming 'ticket' represents the category
                int quantitySold = ticket.getQuantitySold();
                int quantityLeft = ticket.getQuantityLeft();

                Map<String, Integer> categoryInfo = new HashMap<>();
                categoryInfo.put("quantitySold", quantitySold);
                categoryInfo.put("quantityLeft", quantityLeft);

                // Update the map with the list of quantities for this category
                ticketInformation.put(ticketCategory, categoryInfo);
            }

            return ticketInformation;
        } else {
            return new HashMap<>(); // Handle event not found scenario
        }
    }
}
