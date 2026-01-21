package batch.study.w.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class notificationDto {

	private Long id;
	private String message;
	private String targetRole;
	private Integer creDt;
}
