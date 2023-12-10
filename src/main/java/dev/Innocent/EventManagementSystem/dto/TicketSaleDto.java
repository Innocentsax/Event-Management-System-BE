package dev.Innocent.EventManagementSystem.dto;

import dev.Innocent.EventManagementSystem.enums.PaymentType;
import lombok.Data;

import java.util.List;

@Data
public class TicketSaleDto {
    private String name;
    private String email;
    private PaymentType paymentType;
    private List<BookingDto> tickets;
}
