package dev.Innocent.EventManagementSystem.dto;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateRecipientDto {
    private String bankName;
    private String accountNumber;
}

