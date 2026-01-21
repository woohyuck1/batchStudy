package batch.study.w.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "notification_read")
@IdClass(notificationReadId.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class notificationReadEntity {

	@Id
	@Column(name = "notification_id", nullable = false)
	private Long notificationId;

	@Id
	@Column(name = "user_seq", nullable = false)
	private Long userSeq;

	@Column(name = "read_dt")
	private Integer readDt;

	@PrePersist
	protected void onCreate() {
		if (readDt == null) {
			readDt = (int) Instant.now().getEpochSecond();
		}
	}

	@Builder
	public notificationReadEntity(Long notificationId, Long userSeq, Integer readDt) {
		this.notificationId = notificationId;
		this.userSeq = userSeq;
		this.readDt = readDt;
	}
}
