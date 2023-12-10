package dev.Innocent.EventManagementSystem.dto;

import com.decagon.eventbookingsq16.enums.PaymentType;
import lombok.Data;

import java.util.List;

@Data
public class TicketSaleDto {
    private String name;
    private String email;
    private PaymentType paymentType;
    private List<BookingDto> tickets;
}
