package xyz.kmolski.paymentnotification;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailDisplayListener implements ItemWriteListener<SimpleMailMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailDisplayListener.class);

    @Override
    public void beforeWrite(Chunk<? extends SimpleMailMessage> items) {
        LOGGER.info("Sending e-mails: {}", items.getItems());
    }
}
