package dev.Innocent.EventManagementSystem.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Event extends AuditBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String eventTitle;
    @Column(length = 2225)
    private String eventDescription;
    private String location;
    private String category;
    private String banner;
    private String organizer;
    private Integer availableTicket;
    private Integer ticketSold;
    private Integer revenueGenerated;
    private Date startDateTime;
    private Date endDateTime;
    private boolean published;
    private boolean paid = false;


    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets;
    @OneToOne(cascade = CascadeType.ALL)
    private AccountDetails accountDetails;
}

