package dev.Innocent.EventManagementSystem.service;


import dev.Innocent.EventManagementSystem.api.createRecipient.CreateRecipient;
import dev.Innocent.EventManagementSystem.api.paystackpaymentverify.PaystackImpl;
import dev.Innocent.EventManagementSystem.api.verifyaccount.VerifyAccount;
import dev.Innocent.EventManagementSystem.dto.BankNameResponse;
import dev.Innocent.EventManagementSystem.dto.CreateRecipientDto;
import dev.Innocent.EventManagementSystem.dto.TicketSaleDto;
import dev.Innocent.EventManagementSystem.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;


public interface PaymentService {
    ResponseEntity<?> initializeTransaction(TicketSaleDto ticketDto) throws ResourceNotFoundException;

    ResponseEntity<PaystackImpl> verifyTransaction(String email) throws ResourceNotFoundException;

    ResponseEntity<VerifyAccount> verifyAccountDetails(CreateRecipientDto createRecipientDto) throws ResourceNotFoundException;

    BankNameResponse getAllBanks();

    String initiateTransfer() throws ResourceNotFoundException;


    ResponseEntity<CreateRecipient> createRecipient(CreateRecipientDto createRecipientDto) throws ResourceNotFoundException;





}
