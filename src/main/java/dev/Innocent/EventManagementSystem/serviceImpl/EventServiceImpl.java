package dev.Innocent.EventManagementSystem.serviceImpl;


import dev.Innocent.EventManagementSystem.dto.*;
import dev.Innocent.EventManagementSystem.exception.RequestedResourceNotFoundException;
import dev.Innocent.EventManagementSystem.exception.ResourceNotFoundException;
import dev.Innocent.EventManagementSystem.exception.UnAuthorizedException;
import dev.Innocent.EventManagementSystem.model.AccountDetails;
import dev.Innocent.EventManagementSystem.model.Event;
import dev.Innocent.EventManagementSystem.model.Ticket;
import dev.Innocent.EventManagementSystem.model.User;
import dev.Innocent.EventManagementSystem.repository.EventRepository;
import dev.Innocent.EventManagementSystem.repository.TicketRepository;
import dev.Innocent.EventManagementSystem.service.EventServices;
import dev.Innocent.EventManagementSystem.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service

public class EventServiceImpl implements EventServices {
    private final List<String> ticketTypes;
    private final List<String> eventCategory;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final PaymentService paymentService;

    @Autowired
    public EventServiceImpl(List<String> eventCategory, List<String> ticketTypes, EventRepository eventRepository, TicketRepository ticketRepository, PaymentService paymentService) {
        this.ticketTypes = ticketTypes;
        this.eventCategory = eventCategory;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.paymentService=paymentService;

    }

    @Override
    public ResponseEntity<List<String>> allTicketTypes() {
        return new ResponseEntity<>(ticketTypes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<String>> allEventCategory() {
        return new ResponseEntity<>(eventCategory, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EventCategoryTicketTypesBankNames> allTicketsAndCategoryAndBanks() {
        return new ResponseEntity<>(EventCategoryTicketTypesBankNames.builder().eventCategories(eventCategory).ticketTypes(ticketTypes).bankName(paymentService.getAllBanks()) .build(), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<EventDto> saveEvent(EventDto eventDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Event event = EventDtoToEvent(eventDto);
        event.setUser(user);
        Event event1 = eventRepository.save(event);
        List<Ticket> ticketsSpecification = eventDto.getTicketsInfo()
                .stream().map(this::ticketDto2Ticket).collect(Collectors.toList());
        for (Ticket ticket : ticketsSpecification) {
            ticket.setEvent(event1);
        }
        List<Ticket> ticketsSpecification1 = ticketRepository.saveAll(ticketsSpecification);
        EventDto eventDto1 = EventToEventDTo(event1);
        eventDto1.setTicketsInfo(ticketsSpecification1.stream().map(this::ticket2TicketDto).collect(Collectors.toList()));
        publishEvent(event1.getEventId());
        return new ResponseEntity<>(eventDto1, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Ticket>> getEventTickets(Long id) throws ResourceNotFoundException {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Event with the id:" + id + " not found"));
        List<Ticket> tickets = ticketRepository.findAllByEvent(event);

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }


    private Event EventDtoToEvent(EventDto eventDto){
        return Event.builder().eventTitle(eventDto.getEventTitle())
                .eventId(eventDto.getId())
                .eventDescription(eventDto.getEventDescription())
                .availableTicket(eventDto.getAvailableTicket())
                .banner(eventDto.getBannerPath())
                .startDateTime(eventDto.getStartDateTime())
                .endDateTime(eventDto.getEndDateTime())
                .ticketSold(0)
                .category(eventDto.getCategory())
                .revenueGenerated(0)
                .organizer(eventDto.getOrganizer())
                .location(eventDto.getLocation())
                .accountDetails(accountDetailsDtoToAccountDetails(eventDto.getAccountDetailsDto()))
                .tickets(eventDto.getTicketsInfo().stream().map(this::ticketDto2Ticket).collect(Collectors.toList()))
                .build();
    }

    private EventDto EventToEventDTo(Event event) {
        return EventDto.builder()
                .id(event.getEventId())
                .availableTicket(event.getAvailableTicket())
                .eventDescription(event.getEventDescription())
                .location(event.getLocation())
                .bannerPath(event.getBanner())
                .eventTitle(event.getEventTitle())
                .revenueGenerated(event.getRevenueGenerated())
                .startDateTime(event.getStartDateTime())
                .endDateTime(event.getEndDateTime())
                .category(event.getCategory())
                .organizer(event.getCategory())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .accountDetailsDto(accountDetailsToAccountDetailsDto(event.getAccountDetails()))
                .ticketsInfo(event.getTickets().stream().map(this::ticket2TicketDto).collect(Collectors.toList()))
                .build();
    }

    private Ticket ticketDto2Ticket(TicketDto ticketDto) {
        return Ticket.builder()
                .ticketId(ticketDto.getId())
                .ticket(ticketDto.getTicket())
                .cost(ticketDto.getCost())
                .quantityLeft(ticketDto.getQuantityLeft())
                .quantitySold(ticketDto.getQuantitySold())
                .totalQuantity(ticketDto.getTotalQuantity())
                .build();
    }

    private TicketDto ticket2TicketDto(Ticket ticket) {
        return TicketDto.builder()
                .id(ticket.getTicketId())
                .ticket(ticket.getTicket())
                .cost(ticket.getCost())
                .quantityLeft(ticket.getQuantityLeft())
                .quantitySold(ticket.getQuantitySold())
                .totalQuantity(ticket.getTotalQuantity())
                .build();
    }

    private AccountDetails accountDetailsDtoToAccountDetails(AccountDetailsDto accountDetailsDto) {
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setAccountName(accountDetailsDto.getAccountName());
        accountDetails.setId(accountDetailsDto.getId());
        accountDetails.setBankName(accountDetailsDto.getBankName());
        accountDetails.setAccountNumber(accountDetailsDto.getAccountNumber());
        return accountDetails;
    }

    private AccountDetailsDto accountDetailsToAccountDetailsDto(AccountDetails accountDetails) {
        return AccountDetailsDto.builder()
                .id(accountDetails.getId())
                .accountName(accountDetails.getAccountName())
                .accountNumber(accountDetails.getAccountNumber())
                .bankName(accountDetails.getBankName())
                .build();
    }

    @Override
    public ResponseEntity<String> publishEvent(Long eventId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        try {
            Optional<Event> eventOptional = eventRepository.findById(eventId);
            if (!eventOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
            }

            Event event = eventOptional.get();

            if (!user.getId().equals(event.getUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to publish this event");
            }

            if (event.isPublished()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event is already published");
            }

            event.setPublished(true);
            eventRepository.save(event);

            return ResponseEntity.status(HttpStatus.OK).body("Event published successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to publish event");
        }
    }

    @Override
    @Transactional
    public EventDto editEvent(Long eventId, EventDto updateEvent) {
        if (updateEvent == null) {
            throw new IllegalArgumentException("Update event data cannot be null");
        }
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }
        Event existingEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event Not Found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
        Long currentUserId = authenticatedUser.getId();

        if (!existingEvent.getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You don't have permission to update this event");
        }
        if (!existingEvent.isPublished()) {
            throw new AccessDeniedException("You can only edit a published event");
        }
        existingEvent.setPublished(updateEvent.isPublished());
        existingEvent.setEventTitle(updateEvent.getEventTitle());
        existingEvent.setEventDescription(updateEvent.getEventDescription());
        existingEvent.setCategory(updateEvent.getCategory());
        existingEvent.setBanner(updateEvent.getBannerPath());
        existingEvent.setLocation(updateEvent.getLocation());
        existingEvent.setOrganizer(updateEvent.getOrganizer());
        existingEvent.setAvailableTicket(updateEvent.getAvailableTicket());
        existingEvent.setTicketSold(updateEvent.getTicketSold());
        existingEvent.setRevenueGenerated(updateEvent.getRevenueGenerated());
        existingEvent.setStartDateTime(updateEvent.getStartDateTime());
        existingEvent.setEndDateTime(updateEvent.getEndDateTime());

        AccountDetails existingAccountDetails = existingEvent.getAccountDetails();
        AccountDetailsDto updatedAccountDetailsDto = updateEvent.getAccountDetailsDto();

        if (updatedAccountDetailsDto != null) {
            existingAccountDetails.setAccountName(updatedAccountDetailsDto.getAccountName());
            existingAccountDetails.setAccountNumber(updatedAccountDetailsDto.getAccountNumber());
            existingAccountDetails.setBankName(updatedAccountDetailsDto.getBankName());
            existingEvent.setAccountDetails(existingAccountDetails);
        }

        for (TicketDto updatedTicket : updateEvent.getTicketsInfo()) {
            if (updatedTicket.getId() == null) {
                throw new IllegalArgumentException("Ticket ID cannot be null");
            }

            Ticket existingTicket = ticketRepository.findById(updatedTicket.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Ticket Not Found for ID: " + updatedTicket.getId()));
            if (!existingTicket.getEvent().equals(existingEvent)) {
                throw new IllegalArgumentException("Ticket does not belong to the event");
            }
            existingTicket.setCost(updatedTicket.getCost());
            existingTicket.setTotalQuantity(updatedTicket.getTotalQuantity());
            existingTicket.setQuantityLeft(updatedTicket.getTotalQuantity() - existingTicket.getQuantitySold());
            ticketRepository.save(existingTicket);
        }
        existingEvent = eventRepository.save(existingEvent);
        return EventToEventDTo(existingEvent);
    }





    @Override
    public ResponseEntity<String> deleteEvent(Long id) {
        Optional<Event> event= eventRepository. findById(id);
        if(event.isEmpty()){
            throw new RequestedResourceNotFoundException("Event Not found");
        }
        Event event1 =event.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        if(user.getId()!= event1.getUser().getId()){
            throw new UnAuthorizedException("Access denied.You can't delete event you didn't create");
        }
        if(event1.getTicketSold()<1){
            ticketRepository.deleteAll(event1.getTickets());
            eventRepository.delete(event1);
            return new ResponseEntity<>("Event deleted successfully",HttpStatus.OK);
        }
        Date CurrentDate = new Date();
        if(CurrentDate.after(event1.getEndDateTime())){
            return  new ResponseEntity<>("Event has been deleted",HttpStatus.OK);
        }
        return  new ResponseEntity<>("Event cannot be deleted, till event date has passed",HttpStatus.OK);

    }



    public Page<SearchResultDto> searchEvents(String eventTitle, String location, Date startDate, String category, Pageable pageable) {

        List<Event> filteredEvents = filterEvents(eventTitle, location, startDate, category);


        Collections.sort(filteredEvents, Comparator.comparing(Event::getStartDateTime));

        List<SearchResultDto> finalFilteredSearchResultDtos = new ArrayList<>();

        int pageSize = pageable.getPageSize();
        int startIndex = pageable.getPageNumber() * pageSize;
        int endIndex = Math.min(startIndex + pageSize, filteredEvents.size());

        for (int i = startIndex; i < endIndex; i++) {
            Event event = filteredEvents.get(i);
            SearchResultDto searchResultDto = eventToSearchDto(event);
            finalFilteredSearchResultDtos.add(searchResultDto);
        }

        return new PageImpl<>(finalFilteredSearchResultDtos, pageable, filteredEvents.size());
    }

    public List<Event> filterEvents(String eventTitle, String location, Date startDate, String category) {

        eventTitle = (eventTitle != null) ? eventTitle.toLowerCase() : null;
        location = (location != null) ? location.toLowerCase() : null;

        List<Event> filteredEvents;

        if (category.equals("All Category")) {

            filteredEvents = eventRepository.findByPublishedTrue();
        } else {

            filteredEvents = eventRepository.findByPublishedTrueAndCategoryContainingIgnoreCase(category);
        }


        List<Event> resultEvents = new ArrayList<>();
        for (Event event : filteredEvents) {
            boolean includeEvent = true;


            if (eventTitle != null && !event.getEventTitle().toLowerCase().contains(eventTitle)) {
                includeEvent = false;
            }


            if (location != null && !event.getLocation().toLowerCase().contains(location)) {
                includeEvent = false;
            }


            if (startDate != null && event.getStartDateTime().before(startDate)) {
                includeEvent = false;
            }

            if (includeEvent) {
                resultEvents.add(event);
            }
        }

        return resultEvents;
    }




    public SearchResultDto eventToSearchDto(Event event){
        SearchResultDto searchResultDto = new SearchResultDto();

        searchResultDto.setEventTitle(event.getEventTitle());
        searchResultDto.setEventId(event.getEventId());
        searchResultDto.setBanner(event.getBanner());
        searchResultDto.setCategory(event.getCategory());
        searchResultDto.setLocation(event.getLocation());
        searchResultDto.setOrganizer(event.getOrganizer());
        searchResultDto.setAvailableTicket(event.getAvailableTicket());
        searchResultDto.setStartDateTime(event.getStartDateTime());
        searchResultDto.setEndDateTime(event.getEndDateTime());
        searchResultDto.setEventDescription(event.getEventDescription());
        return searchResultDto;
    }


    @Override
    public ResponseEntity<EventDto> viewEvent(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            EventDto eventDto = mapEventToEventDto(event);
            return ResponseEntity.ok(eventDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    private EventDto mapEventToEventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getEventId());
        eventDto.setEventTitle(event.getEventTitle());
        eventDto.setEventDescription(event.getEventDescription());
        eventDto.setAvailableTicket(event.getAvailableTicket());
        eventDto.setBannerPath(event.getBanner());
        eventDto.setStartDateTime(event.getStartDateTime());
        eventDto.setEndDateTime(event.getEndDateTime());
        eventDto.setCategory(event.getCategory());
        eventDto.setOrganizer(event.getOrganizer());
        eventDto.setLocation(event.getLocation());
        eventDto.setCreatedAt(event.getCreatedAt());
        eventDto.setUpdatedAt(event.getUpdatedAt());
        return eventDto;
    }

    @Override
    public List<EventDto> getEventsCreatedByUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Event> events = eventRepository.findByUserId(user.getId());
        System.out.println(user);


        return events.stream()
                .map(this::convertToEventDto)
                .collect(Collectors.toList());
    }
    private EventDto convertToEventDto(Event event) {
        return EventDto.builder()
                .id(event.getEventId())
                .eventTitle(event.getEventTitle())
                .eventDescription(event.getEventDescription())
                .location(event.getLocation())
                .category(event.getCategory())
                .bannerPath(event.getBanner())
                .organizer(event.getOrganizer())
                .availableTicket(event.getAvailableTicket())
                .ticketSold(event.getTicketSold())
                .revenueGenerated(event.getRevenueGenerated())
                .startDateTime(event.getStartDateTime())
                .endDateTime(event.getEndDateTime())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .published(event.isPublished())
                .build();
    }


}
