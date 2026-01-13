package batch.study.w.batch.listener;

import batch.study.w.service.errorLogProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class batchErrorLogListener implements JobExecutionListener {

	private final errorLogProducer logProducer;

	// @Override
	// public void beforeJob(JobExecution jobExecution) {
		
	// }

	@Override
	public void afterJob(JobExecution jobExecution) {
		// Job이 실패한 경우에만 Kafka로 전송
		if (jobExecution.getStatus().isUnsuccessful()) {
			String jobName = jobExecution.getJobInstance().getJobName();
			String errorMessage = "Job execution failed";
			String stackTrace = "";

			// 실패 원인 추출
			if (jobExecution.getFailureExceptions() != null && !jobExecution.getFailureExceptions().isEmpty()) {
				Throwable exception = jobExecution.getFailureExceptions().get(0);
				errorMessage = exception.getMessage() != null ? exception.getMessage() : "Unknown error";
				
				// Stack trace 추출
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				exception.printStackTrace(pw);
				stackTrace = sw.toString();
			}

			// Kafka로 에러 로그 전송
			logProducer.sendLog(Map.of(
				"level", "ERROR",
				"service", "Batch",
				"endpoint", jobName,
				"errorMessage", errorMessage,
				"stackTrace", stackTrace,
				"timestamp", Instant.now().toString()
			));

			log.error("Batch job failed: {} - {}", jobName, errorMessage);
		}
	}
}
