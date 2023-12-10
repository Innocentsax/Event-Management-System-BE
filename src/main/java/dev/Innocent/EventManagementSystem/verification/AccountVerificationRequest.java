package dev.Innocent.EventManagementSystem.verification;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountVerificationRequest {
    private String accountNumber;
    private String bankCode;

}

