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
@Table(name = "user_point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class userPointEntity extends baseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_point_seq", nullable = false)
	private Long userPointSeq;

	@Column(name = "user_seq")
	private Long userSeq;

	@Column(name = "point")
	private Integer point;

	@Builder
	public userPointEntity(Long userSeq, Integer point) {
		this.userSeq = userSeq;
		this.point = point;
	}

	public void updatePoint(Integer point) {
		this.point = point;
	}
}

