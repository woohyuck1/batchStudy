package batch.study.w.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 인증 관련
	USER_NOT_FOUND("U001", "사용자를 찾을 수 없습니다."),
	INVALID_PASSWORD("U002", "비밀번호가 일치하지 않습니다."),
	LOGIN_FAILED("U003", "아이디 또는 비밀번호가 올바르지 않습니다."),

	INVALID_INPUT_VALUE("S001", "잘못된 입력값입니다."),
	INTERNAL_SERVER_ERROR("S002", "서버 오류가 발생했습니다. 관리자에게 문의하세요."),

	//엑셀
	INVALID_FILE_TYPE("E001", "Excel 파일만 업로드 가능합니다."),
	EXCEL_READ_ERROR("E002", "Excel 파일을 읽는 중 오류가 발생했습니다."),
	FILE_SIZE_EXCEEDED("E003", "파일 크기가 너무 큽니다. 최대 500MB까지 지원됩니다."),
	EXCEL_COLUMN_NOT_FOUND("E004", "Excel 파일에서 필수 컬럼을 찾을 수 없습니다."),
	
	// JWT 관련
	UNAUTHORIZED("J001", "인증이 이상해용.");

	private final String code;
	private final String message;
}


