package batch.study.w.common.exception;

import batch.study.w.common.response.apiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<apiResponse<Void>> handleRuntimeException(RuntimeException e) {
		log.error("RuntimeException ---> ", e.getMessage());
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(apiResponse.error(e.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<apiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("IllegalArgumentException --------->: ", e.getMessage());
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(apiResponse.error(e.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<apiResponse<Void>> handleException(Exception e) {
		log.error("Exception ----------> ", e.getMessage());
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(apiResponse.error("서버 오류 ---> " + e.getMessage()));
	}
}

