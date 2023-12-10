package dev.Innocent.EventManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {
    private Long id;
    private String ticket;
    private double cost;
    private int totalQuantity;
    private int quantitySold=0;
    private int quantityLeft;
}
