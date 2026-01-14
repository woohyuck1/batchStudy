package batch.study.w.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Point 증가 Job 스케줄러
 * 
 * 매일 자정(00:00:00)에 pointIncrementJob을 실행
 * 
 * - 배치 재시도는 운영자가
 * - 재실행은 Spring Batch가
 * - Scheduler는 "트리거만"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class pointIncrementScheduler {

	private final JobLauncher jobLauncher;
	private final Job pointIncrementJob;

	/**
	 * 매일 자정(00:00:00)에 실행
	 * 
	 * Cron 표현식: "0 0 0 * * ?"
	 * - 초: 0
	 * - 분: 0
	 * - 시: 0
	 * - 일: 0 
	 * - 월: 0 
	 * - 요일: ? (특정 요일 지정 안함)
	 * 
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void runPointIncrementJob() {
		try {
			log.info("Point 증가 Job 시작");
			
			// runDate 기반 JobParameters: 같은 날 중복 실행 방지
			//	JobParameters = 배치 작업에 전달되는 파라미터
			JobParameters jobParameters = new JobParametersBuilder()
				.addString("runDate", LocalDate.now().toString())
				.toJobParameters();
			
			jobLauncher.run(pointIncrementJob, jobParameters); // pointIncrementJob 필드이름과 bean 이름이 같아서 spring 이 자동으로 주입
			
			log.info("Point 증가 Job 완료");
			
		} catch (Exception e) {
			log.error("Point 증가 Job 실패", e);
		}
	}
}

