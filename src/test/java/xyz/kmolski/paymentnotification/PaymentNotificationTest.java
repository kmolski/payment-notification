package xyz.kmolski.paymentnotification;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@SpringBatchTest
class PaymentNotificationTest {

	@Autowired
	JobLauncherTestUtils jobLauncherTestUtils;

	public static void main(String[] args) {
		PaymentNotification.main(args);
	}

	@Test
	void stepExecutesCorrectly(@Autowired Job job) {
		jobLauncherTestUtils.setJob(job);
		var stepExecution = jobLauncherTestUtils.launchStep("check-payment-and-send");
		assertEquals("COMPLETED", stepExecution.getExitStatus().getExitCode());
	}

	@Test
	void jobExecutesCorrectly(@Autowired Job job) throws Exception {
		jobLauncherTestUtils.setJob(job);
		var jobExecution = jobLauncherTestUtils.launchJob();
		assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
	}
}
