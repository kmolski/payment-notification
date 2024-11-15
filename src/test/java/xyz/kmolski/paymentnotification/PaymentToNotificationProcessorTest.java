package xyz.kmolski.paymentnotification;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentToNotificationProcessorTest {

    PaymentToNotificationProcessor processor = new PaymentToNotificationProcessor();

    @Test
    void checkPaymentIsNearDueDate() {
        var payment = new Payment(0L, Date.valueOf(LocalDate.now()), BigDecimal.ZERO, "");
        var result = processor.process(payment);
        assertNotNull(result);
    }
}
