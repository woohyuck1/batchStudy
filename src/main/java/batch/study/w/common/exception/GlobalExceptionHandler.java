package batch.study.w.common.exception;

import batch.study.w.common.response.apiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<apiResponse<Void>> handleBusinessException(BusinessException e) {
		log.error("BusinessException ---> [{}] {}", e.getCode(), e.getMessage());
		
	
		if ("J001".equals(e.getCode())) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(apiResponse.error(e.getMessage(), e.getCode()));
		}
		
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(apiResponse.error(e.getMessage(), e.getCode()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<apiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
		FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
		String errorMessage = fieldError.getDefaultMessage();
		log.error("ValidationException ---> {}", errorMessage);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(apiResponse.error(errorMessage));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<apiResponse<Void>> handleRuntimeException(RuntimeException e) {
		log.error("RuntimeException ---> ", e);
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(apiResponse.error(e.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<apiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("IllegalArgumentException --------->: ", e);
		String message = e.getMessage();
		if (message != null && (message.contains("JWT") || message.contains("HMAC-SHA") || message.contains("key byte array"))) {
			return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(apiResponse.error("관리자에게 문의하세요."));
		}
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(apiResponse.error("잘못된 요청입니다."));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<apiResponse<Void>> handleException(Exception e) {
		log.error("Exception ----------> ", e);
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(apiResponse.error("서버 오류가 발생했습니다."));
	}
}

