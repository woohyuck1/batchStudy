package batch.study.w.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class messageDto {

	private String message;
	private String targetRole;

	public static messageDto of(String message) {
		return messageDto.builder()
			.message(message)
			.build();
	}

	public static messageDto of(String message, Object data) {
		return messageDto.builder()
			.message(message)
			.build();
	}

	public static messageDto of(String message, String targetRole) {
		return messageDto.builder()
			.message(message)
			.targetRole(targetRole)
			.build();
	}
}
