package batch.study.w.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Point 증가 Batch Job 설정
 * 
 * 매일 모든 사용자의 point를 +1씩 증가시키는 Job
 * 
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class pointIncrementJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final JdbcTemplate jdbcTemplate;

	/**
	 * Point 증가 Job
	 * 
	 */
	@Bean
	public Job pointIncrementJob(Step pointIncrementStep) {
		return new JobBuilder("pointIncrementJob", jobRepository)
			.start(pointIncrementStep)
			.build();
	}

	/**
	 * Point 증가 Step
	 */
	@Bean
	public Step pointIncrementStep() {
		return new StepBuilder("pointIncrementStep", jobRepository)
			.tasklet(pointIncrementTasklet(), transactionManager)
			.build();
	}

	/**
	 * Point 증가 Tasklet
	 * UPDATE user_point SET point = point + 1, increment_dt = ? WHERE del_yn = 0
	 * 
	 * increment_dt를 체크하여 오늘 이미 증가된 경우는 제외
	 */
	@Bean
	public Tasklet pointIncrementTasklet() {
		return (contribution, chunkContext) -> {
			log.info("Point 증가 작업 시작!!!");
			
			// 오늘 날짜의 시작 시간 (00:00:00) 계산
			java.time.LocalDate today = java.time.LocalDate.now();
			java.time.LocalDateTime startOfDay = today.atStartOfDay();
			int todayStartTimestamp = (int) startOfDay.atZone(java.time.ZoneId.systemDefault())
				.toEpochSecond();
			
			// increment_dt가 오늘 날짜가 아닌 경우만 업데이트 (오늘 이미 증가된 것은 제외)
			String updateSql = "UPDATE user_point " +
				"SET point = point + 1, increment_dt = ? " +
				"WHERE del_yn = 0 " +
				"AND (increment_dt IS NULL OR increment_dt < ?)";
			
			int updatedCount = jdbcTemplate.update(
				updateSql,
				todayStartTimestamp,  // increment_dt에 오늘 날짜 설정
				todayStartTimestamp    // 오늘 날짜보다 작은 것만 업데이트
			);
			
			log.info("Point 증가 작업 완료: {}건 업데이트", updatedCount);
			
			return RepeatStatus.FINISHED;
		};
	}
}

