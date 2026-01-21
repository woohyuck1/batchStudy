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
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class userEntity extends baseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_seq", nullable = false)
	private Long userSeq;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "roles")
	private String roles;

	@Builder
	public userEntity(String userId, String userName, String password, String roles) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.roles = roles;
	}

	public void updateUserName(String userName) {
		this.userName = userName;
	}

	public void updatePassword(String password) {
		this.password = password;
	}
}
