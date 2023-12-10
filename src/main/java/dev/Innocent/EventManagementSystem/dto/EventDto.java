package dev.Innocent.EventManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {
    private Long id;
    private String eventTitle;
    private String eventDescription;
    private String location;
    private String category;
    private String bannerPath;
    private String organizer;
    private int availableTicket;
    private int ticketSold;
    private int revenueGenerated;
    private List<TicketDto> ticketsInfo;
    private AccountDetailsDto accountDetailsDto;
    private Date startDateTime;
    private Date endDateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean published;


}



