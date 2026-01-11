package batch.study.w.service;

import batch.study.w.dto.loginRequestDto;
import batch.study.w.dto.loginResponseDto;
import batch.study.w.dto.userDto;
import batch.study.w.dto.userRequestDto;

import java.util.List;

public interface userService {

	List<userDto> findAllUsers();

	userDto save(userRequestDto userRequestDto);

	loginResponseDto login(loginRequestDto loginRequestDto);
}

