package batch.study.w.dto;

import batch.study.w.entity.userEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class userDto {

	private Long userSeq;
	private String userId;
	private String userName;
	private String password;
	private String roles;
	private Integer creDt;
	private Integer delYn;

	public static userDto from(userEntity userEntity) {
		return userDto.builder()
			// .userSeq(userEntity.getUserSeq())
			.userId(userEntity.getUserId())
			.userName(userEntity.getUserName())
			.roles(userEntity.getRoles())
			.creDt(userEntity.getCreDt())
			.delYn(userEntity.getDelYn())
			.build();
	}
}
