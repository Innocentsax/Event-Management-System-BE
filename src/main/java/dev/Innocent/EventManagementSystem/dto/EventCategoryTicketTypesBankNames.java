package dev.Innocent.EventManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventCategoryTicketTypesBankNames {
    private List<String> ticketTypes;
    private List<String> eventCategories;
    private BankNameResponse bankName;

}
