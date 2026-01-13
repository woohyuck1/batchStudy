package batch.study.w.dto;

import batch.study.w.entity.errorLogEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class errorLogDto {

	private Long errorLogId;
	private String level;
	private String serviceName;
	private String endpoint;
	private String requestData;
	private String errorMessage;
	private String stackTrace;
	private String requestId;
	private Integer creDt;

	public static errorLogDto from(errorLogEntity errorLogEntity) {
		return errorLogDto.builder()
			.errorLogId(errorLogEntity.getErrorLogId())
			.level(errorLogEntity.getLevel())
			.serviceName(errorLogEntity.getServiceName())
			.endpoint(errorLogEntity.getEndpoint())
			.requestData(errorLogEntity.getRequestData())
			.errorMessage(errorLogEntity.getErrorMessage())
			.stackTrace(errorLogEntity.getStackTrace())
			.requestId(errorLogEntity.getRequestId())
			.creDt(errorLogEntity.getCreDt())
			.build();
	}
}
