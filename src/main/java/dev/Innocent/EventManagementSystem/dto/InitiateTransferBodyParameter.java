package dev.Innocent.EventManagementSystem.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitiateTransferBodyParameter {
    private String source;
    private String password;
    private Integer amount;
    private String recipient;
    private String reason;
    private String currency;
}
