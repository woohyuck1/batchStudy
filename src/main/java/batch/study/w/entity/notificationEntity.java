package batch.study.w.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class notificationEntity extends baseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id", nullable = false)
	private Long notificationId;

	@Column(name = "message", length = 1000)
	private String message;

	@Column(name = "target_role", length = 100)
	private String targetRole;

	@Column(name = "expires_at")
	private Integer expiresAt;

	@Builder
	public notificationEntity(String message, String targetRole) {
		this.message = message;
		this.targetRole = targetRole;
	}
}
