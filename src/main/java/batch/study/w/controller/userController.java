package batch.study.w.controller;

import batch.study.w.common.response.apiResponse;
import batch.study.w.dto.loginRequestDto;
import batch.study.w.dto.loginResponseDto;
import batch.study.w.dto.userDto;
import batch.study.w.dto.userRequestDto;
import batch.study.w.service.authService;
import batch.study.w.service.userService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class userController {

	private final userService userService;
	private final authService authService;

	@GetMapping
	public ResponseEntity<apiResponse<List<userDto>>> getAllUsers() {
		List<userDto> users = userService.findAllUsers();
		return ResponseEntity.ok(apiResponse.success(users));
	}

	@PostMapping("/signup")
	public ResponseEntity<apiResponse<userDto>> signup(@Valid @RequestBody userRequestDto userRequestDto) {
		userDto savedUser = userService.save(userRequestDto);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(apiResponse.success(savedUser));
	}

	@PostMapping("/login")
	public ResponseEntity<apiResponse<loginResponseDto>> login(@Valid @RequestBody loginRequestDto loginRequestDto) {
		loginResponseDto loginResponse = authService.login(loginRequestDto);
		return ResponseEntity.ok(apiResponse.success(loginResponse));
	}

	@Operation(summary = "Excel 파일 업로드 및 읽기")
	@PostMapping(
		value = "/excel",
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<apiResponse<Map<String, List<List<String>>>>> readExcel(
		@Parameter(
			description = "Excel 파일 (.xlsx, .xls)",
			required = true,
			content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(type = "string", format = "binary"))
		)
		@RequestParam("file") MultipartFile file
	) {
		Map<String, List<List<String>>> excelData = userService.readExcel(file);
		return ResponseEntity.ok(apiResponse.success("Excel 파일 읽기에 성공했습니다.", excelData));
	}

	@Operation(summary = "Excel 파일 업로드 및 사용자 일괄 등록")
	@PostMapping(
		value = "/excel/save",
		consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<apiResponse<List<userDto>>> saveFromExcel(
		@Parameter(
			description = "Excel 파일 (.xlsx, .xls) - 첫 번째 시트의 첫 번째 행은 헤더(선택), 데이터는 userId, userName, password 순서",
			required = true,
			content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(type = "string", format = "binary"))
		)
		@RequestParam("file") MultipartFile file
	) {
		List<userDto> savedUsers = userService.saveFromExcel(file);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(apiResponse.success(String.format("%d명의 사용자가 등록되었습니다.", savedUsers.size()), savedUsers));
	}
}
