package batch.study.w.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;

import java.time.Instant;

@MappedSuperclass
@Getter
public abstract class baseEntity {

	@Column(name = "cre_dt")
	private Integer creDt;

	@Column(name = "del_yn")
	private Integer delYn = 0;

	@PrePersist
	protected void onCreate() {
		if (creDt == null) {
			// 현재 시간을 Unix timestamp (초 단위)로 변환
			creDt = (int) Instant.now().getEpochSecond();
		}
		if (delYn == null) {
			delYn = 0;
		}
	}

	public void delete() {
		this.delYn = 1;
	}
}
