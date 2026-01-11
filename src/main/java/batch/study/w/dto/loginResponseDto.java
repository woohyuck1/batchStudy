package batch.study.w.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class loginResponseDto {

	private String accessToken;
	@Builder.Default
	private String tokenType = "Bearer";
	private Long userSeq;
	private String userId;
	private String userName;
}

