package batch.study.w.dto;

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
	private Integer creDt;
	private Integer delYn;
}
