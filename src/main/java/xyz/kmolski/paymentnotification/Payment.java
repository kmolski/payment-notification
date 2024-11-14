package xyz.kmolski.paymentnotification;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public record Payment(long orderId, Date dueDate, BigDecimal amount, String email) {

    public boolean isNearDueDate() {
        return dueDate.toLocalDate().minusDays(2).isBefore(LocalDate.now());
    }
}
