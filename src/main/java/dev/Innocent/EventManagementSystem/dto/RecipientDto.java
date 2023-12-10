package dev.Innocent.EventManagementSystem.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipientDto {
    private String type;
    private String name;
    private String account_number;
    private String bank_code;
    private String currency;
}

