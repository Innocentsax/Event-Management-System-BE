package dev.Innocent.EventManagementSystem.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SearchResultDto {
    private Long eventId;



    private String eventTitle;

    private String eventDescription;
    private String location;
    private String category;
    private String banner;
    private String organizer;
    private Integer availableTicket;
    private Date startDateTime;
    private Date endDateTime;

}
