package dev.Innocent.EventManagementSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class EmailVerification extends VerificationBaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private boolean verified;

    public void calculateExpirationTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 2880);
        this.setExpirationTime(calendar.getTime());

    }
}
