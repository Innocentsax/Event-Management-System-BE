package dev.Innocent.EventManagementSystem.controller;


import dev.Innocent.EventManagementSystem.api.createRecipient.CreateRecipient;
import dev.Innocent.EventManagementSystem.api.paystackpaymentverify.PaystackImpl;
import dev.Innocent.EventManagementSystem.api.verifyaccount.VerifyAccount;
import dev.Innocent.EventManagementSystem.dto.BankNameResponse;
import dev.Innocent.EventManagementSystem.dto.CreateRecipientDto;
import dev.Innocent.EventManagementSystem.dto.TicketSaleDto;
import dev.Innocent.EventManagementSystem.exception.ResourceNotFoundException;
import dev.Innocent.EventManagementSystem.service.DatabaseSeedService;
import dev.Innocent.EventManagementSystem.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final DatabaseSeedService databaseSeedService;

    @PostMapping("/initializeTransaction")
    public ResponseEntity<?> initializeTransaction(@RequestBody TicketSaleDto ticketDto) throws ResourceNotFoundException {
        return paymentService.initializeTransaction(ticketDto);
    }

    @GetMapping("/verify_transaction")
    public RedirectView verifyTransaction(@RequestParam("trxref") String reference) throws ResourceNotFoundException {
        ResponseEntity<PaystackImpl> response = paymentService.verifyTransaction(reference);
        RedirectView redirectView = new RedirectView();
        if (response.getBody().getData().getStatus().equals("success")) {
            redirectView.setUrl("http://localhost:3000/#/skills");
        }
        return redirectView;
    }

    @GetMapping("/banks/verify")
    public ResponseEntity<VerifyAccount> verifyAccountDetails(@RequestBody CreateRecipientDto createRecipientDto) throws ResourceNotFoundException {
        return paymentService.verifyAccountDetails(createRecipientDto);
    }

   @GetMapping("/bank/list")
    public ResponseEntity<BankNameResponse> getAllBanks(){
      return new ResponseEntity<>(paymentService.getAllBanks(), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public String initiateTransfer() throws ResourceNotFoundException {
        return paymentService.initiateTransfer();
    }

    @PostMapping("/transfer/recipient")
    public ResponseEntity<CreateRecipient> createRecipient(@RequestBody CreateRecipientDto createRecipientDto) throws ResourceNotFoundException {
        return paymentService.createRecipient(createRecipientDto);
    }


}

