package batch.study.w.controller;

import batch.study.w.common.response.apiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 배치 작업 관리 API
 */
@Tag(name = "Batch", description = "배치 작업 관리 API")
@Slf4j
@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class batchController {

	private final JobLauncher jobLauncher;
	private final Job pointIncrementJob;

	@Operation(
		summary = "Point 증가 배치 작업 수동 실행",
		description = "스케줄이 끝났는데 새로운 데이터가 들어올때 실행하깅"
	)
	@ApiResponses({@ApiResponse(responseCode = "200", description = "성공"),})
	@PostMapping("/point-increment")
	public ResponseEntity<apiResponse<String>> runPointIncrementJob() {
		try {
			log.info("Point 증가 Job 수동 실행 시작");

			// runDate 기반 JobParameters: 같은 날 중복 실행 방지
			JobParameters jobParameters = new JobParametersBuilder()
				.addString("runDate", LocalDate.now().toString())
				.addLong("manualExecution", System.currentTimeMillis())  // 수동 실행 구분
				.toJobParameters();

			jobLauncher.run(pointIncrementJob, jobParameters);

			log.info("Point 증가 Job 수동 실행 완료");

			return ResponseEntity.ok(
				apiResponse.success("Point 증가 배치 작업이 성공적으로 실행되었습니다.")
			);

		} catch (Exception e) {
			log.error("Point 증가 Job 수동 실행 실패", e);
			return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(apiResponse.error("배치 작업 실행 중 오류가 발생했습니다: " + e.getMessage()));
		}
	}
}

