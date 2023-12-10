package dev.Innocent.EventManagementSystem.serviceImpl;


import dev.Innocent.EventManagementSystem.api.createRecipient.CreateRecipient;
import dev.Innocent.EventManagementSystem.api.initiateTransfer.InitiateTransfer;
import dev.Innocent.EventManagementSystem.api.paystackpaymentinit.PaystackResponse;
import dev.Innocent.EventManagementSystem.api.paystackpaymentverify.PaystackImpl;
import dev.Innocent.EventManagementSystem.api.verifyaccount.VerifyAccount;
import dev.Innocent.EventManagementSystem.dto.*;
import dev.Innocent.EventManagementSystem.enums.PaymentType;
import dev.Innocent.EventManagementSystem.exception.ResourceNotFoundException;
import dev.Innocent.EventManagementSystem.mail.EmailService2;
import dev.Innocent.EventManagementSystem.model.*;
import dev.Innocent.EventManagementSystem.repository.*;
import dev.Innocent.EventManagementSystem.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.paystack.co")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer sk_test_cd7c0f64d9aa4c1b2b9b31a004e96d7a88fb6bd2")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    private final BankRepository bankRepository;
    private final UserTransactionRepository userPaymentRepository;
    private final String CALL_BACK_URL = "http://localhost:8088/verify_transaction";
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final AccountDetailsRepository accountDetailsRepository;
    private final EmailServiceImpl emailService;
    private final EmailService2 emailService2;



    public ResponseEntity<?> initializeTransaction(TicketSaleDto ticketDto) throws ResourceNotFoundException {
        double price = 0;
        for (BookingDto ticket : ticketDto.getTickets()) {
            price += ticket.getCost();
        }
        String reference = UUID.randomUUID().toString();
        UserTransactionInfo paymentInfo = UserTransactionInfo.builder()
                .name(ticketDto.getName())
                .email(ticketDto.getEmail())
                .amount(price)
                .currency("NGN")
                .callBackUrl(CALL_BACK_URL)
                .date(new Date())
                .reference(reference)
                .build();

        if (ticketDto.getPaymentType().equals(PaymentType.PAYSTACK)) {
            PaystackResponse initiateResponse = webClient
                    .post()
                    .uri("/transaction/initialize")
                    .bodyValue(paymentInfo)
                    .retrieve()
                    .bodyToMono(PaystackResponse.class)
                    .block();

            if (initiateResponse != null) {
                userPaymentRepository.save(paymentInfo);
                return new ResponseEntity<>(initiateResponse, HttpStatus.OK);
            }

        }

        return new ResponseEntity<>("Payment type not available", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<PaystackImpl> verifyTransaction(String reference) throws ResourceNotFoundException {
        UserTransactionInfo transactionInfo = userPaymentRepository.findByReference(reference).orElseThrow(
                () -> new ResourceNotFoundException("Transaction has not been verified")
        );
        log.info("I got here -----------");
        PaystackImpl response = webClient
                .get()
                .uri("/transaction/verify/" + reference)
                .retrieve()
                .bodyToMono(PaystackImpl.class).block();

        if (response != null) {
            if (response.getData().getStatus().equals("success")) {
                transactionInfo.setVerified(true);
                userPaymentRepository.save(transactionInfo);
                log.info("Hello verify ---------");
                System.out.println("got here");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return null;
    }



    public ResponseEntity<VerifyAccount> verifyAccountDetails(CreateRecipientDto createRecipientDto) throws ResourceNotFoundException {
        Bank bank = bankRepository.findByName(createRecipientDto.getBankName()).orElseThrow(
                () -> new ResourceNotFoundException("Bank not found"));
        VerifyAccount verifiedAccount = webClient.get()
                .uri("/bank/resolve?account_number="+ createRecipientDto.getAccountNumber() +"&bank_code=" + bank.getCode())
                .retrieve()
                .bodyToMono(VerifyAccount.class)
                .block();
        return  new ResponseEntity<>(verifiedAccount, HttpStatus.OK);
    }



    @Override
    public BankNameResponse getAllBanks() {
        List<Bank> bankList = bankRepository.findAll();
        return BankNameResponse.builder().bankNames(bankList.stream().map(this::bankToString).collect(Collectors.toList())).build();
    }

    @Scheduled(cron = "0 * * * * ?")
    public void checkEvent7DaysOld() throws ResourceNotFoundException {
        initiateTransfer();
    }

    @Override
    public String initiateTransfer() throws ResourceNotFoundException {
        List<Event> events = eventRepository.findAll();

        for(Event event : events) {
            if (event.isPaid()) {
                return "You've being paid";
            }

            User user = event.getUser();
            AccountDetails accountDetails = event.getAccountDetails();

            CreateRecipientDto createRecipientDto = CreateRecipientDto.builder()
                    .bankName(accountDetails.getBankName())
                    .accountNumber(String.valueOf(accountDetails.getAccountNumber()))
                    .build();

            Date currentDate = new Date();

            if (currentDate.after(event.getEndDateTime())) {
                InitiateTransferBodyParameter bodyParameter = InitiateTransferBodyParameter.builder()
                        .source("balance")
                        .amount(event.getRevenueGenerated())
                        .recipient(Objects.requireNonNull(createRecipient(createRecipientDto).getBody()).getData().getRecipientCode())
                        .reason("Calm down")
//                    .reference(UUID.randomUUID().toString())
                        .build();
                try {
                    InitiateTransfer initiateTransfer = webClient.post()
                            .uri("/transfer")
                            .bodyValue(bodyParameter)
                            .retrieve()
                            .bodyToMono(InitiateTransfer.class)
                            .block();

                } catch (WebClientResponseException e) {
                    e.printStackTrace();
                    event.setPaid(true);
                    eventRepository.save(event);
                    emailService2.sendMail(user.getUsername(), EmailServiceImpl.PAY_SUBJECT, EmailServiceImpl.PAY_CONTENT2);
                }
            }
        }
        return "Check your email for payment status";
    }


    @Override
    public ResponseEntity<CreateRecipient> createRecipient(CreateRecipientDto createRecipientDto) throws ResourceNotFoundException {
        Bank bank = bankRepository.findByName(createRecipientDto.getBankName()).orElseThrow(
                () -> new ResourceNotFoundException("Bank not found"));

        VerifyAccount verifiedAccount = verifyAccountDetails(createRecipientDto).getBody();

        if (verifiedAccount != null) {
            String accountName = verifiedAccount.getData().getAccountName();
            RecipientDto recipientDto = RecipientDto.builder()
                    .type("nuban")
                    .name(accountName)
                    .account_number(createRecipientDto.getAccountNumber())
                    .bank_code(bank.getCode())
                    .currency("NGN")
                    .build();

            CreateRecipient createRecipient = webClient.post()
                    .uri("/transferrecipient")
                    .bodyValue(recipientDto)
                    .retrieve()
                    .bodyToMono(CreateRecipient.class)
                    .block();
            if (createRecipient != null) {
                return new ResponseEntity<>(createRecipient, HttpStatus.OK);
            }
        } else throw new ResourceNotFoundException("Bank not found");
        return null;

    }

    private String bankToString(Bank bank){
      return bank.getName();
    }


}

