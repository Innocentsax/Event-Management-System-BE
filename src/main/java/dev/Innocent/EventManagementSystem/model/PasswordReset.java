package dev.Innocent.EventManagementSystem.model;

import com.decagon.eventbookingsq16.util.TimestampUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.UUID;

import static com.decagon.eventbookingsq16.util.TimestampUtil.EXPIRATION_MINUTES;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class PasswordReset extends VerificationBaseEntity{
    private Boolean isValid;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    public PasswordReset(User user){
        super();
        this.user = user;
        EXPIRATION_MINUTES = 15;
        setExpirationTime(TimestampUtil.calculateExpirationTime().getTime());
    }
    public String generateToken(){
        String resetToken = UUID.randomUUID().toString();
        long expirationTimeInMillis = calculateExpirationTimeMillis();
        resetToken += expirationTimeInMillis;
        return resetToken;
    }
    private long calculateExpirationTimeMillis() {
        Calendar expirationTime = Calendar.getInstance();
        expirationTime.add(Calendar.MINUTE, EXPIRATION_MINUTES);
        return expirationTime.getTimeInMillis();
    }

}
