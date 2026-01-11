package batch.study.w.controller;

import batch.study.w.common.response.apiResponse;
import batch.study.w.dto.userDto;
import batch.study.w.service.userService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class userController {

	private final userService userService;

	@GetMapping
	public ResponseEntity<apiResponse<List<userDto>>> getAllUsers() {
		List<userDto> users = userService.findAll();
		return ResponseEntity.ok(apiResponse.success(users));
	}
}
