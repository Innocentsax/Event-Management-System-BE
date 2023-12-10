package dev.Innocent.EventManagementSystem.dto;

import lombok.Data;

@Data
public class BookingDto {
    private String ticketType;
    private Integer quantity;
    private double cost;
}
