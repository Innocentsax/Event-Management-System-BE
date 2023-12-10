package dev.Innocent.EventManagementSystem.model;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class TicketSale extends BaseEntity{
    private String fullName;
    private String email;
    private String phoneNumber;
    private String ticketType;
    private Integer ticketNumber;
    private Integer quantityBought;
    @ManyToOne
    private Event event;

}
