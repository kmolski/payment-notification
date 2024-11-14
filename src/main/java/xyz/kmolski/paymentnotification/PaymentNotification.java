package xyz.kmolski.paymentnotification;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.mail.SimpleMailMessageItemWriter;
import org.springframework.batch.item.mail.builder.SimpleMailMessageItemWriterBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@SpringBootApplication
public class PaymentNotification {

    @Bean
    public Job notifyPayment(JobRepository jobRepository, Step checkPaymentAndSend) {
        return new JobBuilder("payment-notification", jobRepository)
                .start(checkPaymentAndSend)
                //.preventRestart()
                .build();
    }

    @Bean
    public Step checkPaymentAndSend(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            JdbcCursorItemReader<Payment> paymentReader,
            ItemProcessor<Payment, SimpleMailMessage> paymentToNotificationProcessor,
            SimpleMailMessageItemWriter mailWriter
    ) {
        return new StepBuilder("check-payment-and-send", jobRepository)
                .<Payment, SimpleMailMessage>chunk(10, txManager)
                .reader(paymentReader)
                .processor(paymentToNotificationProcessor)
                .writer(mailWriter)
                .build();
    }

    @Bean
    JdbcCursorItemReader<Payment> paymentReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Payment>()
                .dataSource(dataSource)
                .name("PaymentReader")
                .sql("""
                     SELECT ORDER_ID, DUE_DATE, AMOUNT, EMAIL FROM PAYMENT
                     """)
                .rowMapper(new DataClassRowMapper<>(Payment.class))
                .build();
    }

    @Bean
    SimpleMailMessageItemWriter mailWriter(MailSender mailSender) {
        return new SimpleMailMessageItemWriterBuilder()
                .mailSender(mailSender)
                .build();
    }

    public static void main(String[] args) {
        System.exit(
            SpringApplication.exit(
                SpringApplication.run(PaymentNotification.class, args)));
    }
}
