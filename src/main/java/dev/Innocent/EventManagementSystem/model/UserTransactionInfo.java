package dev.Innocent.EventManagementSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transaction-info")
public class UserTransactionInfo extends BaseEntity{
    private String name;
    private String email;
    private Double amount;
    private String currency;
    private String reference;
    private String callBackUrl;
    private Date date;
    private boolean isVerified = false;
}
