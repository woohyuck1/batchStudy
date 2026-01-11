package batch.study.w.controller;

import batch.study.w.common.response.apiResponse;
import batch.study.w.dto.loginRequestDto;
import batch.study.w.dto.loginResponseDto;
import batch.study.w.dto.userDto;
import batch.study.w.dto.userRequestDto;
import batch.study.w.service.userService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class userController {

	private final userService userService;

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
		loginResponseDto loginResponse = userService.login(loginRequestDto);
		return ResponseEntity.ok(apiResponse.success(loginResponse));
	}
}
