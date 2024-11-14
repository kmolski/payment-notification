package xyz.kmolski.paymentnotification;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class PaymentToNotificationProcessor implements ItemProcessor<Payment, SimpleMailMessage> {

    @Override
    public SimpleMailMessage process(Payment payment) throws Exception {
        if (payment.isNearDueDate()) {
            var message = new SimpleMailMessage();
            message.setTo(payment.email());
            message.setFrom("payments@example.com");
            message.setSubject("Your payment for order %s is due".formatted(payment.orderId()));
            message.setText("Your payment of %s is due on %s".formatted(payment.amount(), payment.dueDate()));
            return message;
        } else {
            return null;
        }
    }
}
