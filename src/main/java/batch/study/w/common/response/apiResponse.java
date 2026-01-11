package batch.study.w.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class apiResponse<T> {

	private boolean success;
	private String message;
	private T data;

	public static <T> apiResponse<T> success(T data) {
		return new apiResponse<>(true, "성공", data);
	}

	public static <T> apiResponse<T> success(String message, T data) {
		return new apiResponse<>(true, message, data);
	}

	public static <T> apiResponse<T> error(String message) {
		return new apiResponse<>(false, message, null);
	}

	public static <T> apiResponse<T> error(String message, T data) {
		return new apiResponse<>(false, message, data);
	}
}
