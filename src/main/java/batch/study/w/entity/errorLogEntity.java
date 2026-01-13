package batch.study.w.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "error_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class errorLogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "error_log_id", nullable = false)
	private Long errorLogId;

	@Column(name = "level")
	private String level;

	@Column(name = "service_name")
	private String serviceName;

	@Column(name = "endpoint")
	private String endpoint;

	@Column(name = "request_data")
	private String requestData;

	@Column(name = "error_message")
	private String errorMessage;

	@Column(name = "stack_trace")
	private String stackTrace;

	@Column(name = "request_id")
	private String requestId;

	@Column(name = "creDt")
	private Integer creDt;

	@PrePersist
	protected void onCreate() {
		if (creDt == null) {

			creDt = (int) Instant.now().getEpochSecond();
		}
	}

	@Builder
	public errorLogEntity(String level, String serviceName, String endpoint, 
	                      String requestData, String errorMessage, String stackTrace, 
	                      String requestId) {
		this.level = level;
		this.serviceName = serviceName;
		this.endpoint = endpoint;
		this.requestData = requestData;
		this.errorMessage = errorMessage;
		this.stackTrace = stackTrace;
		this.requestId = requestId;
	}
}
