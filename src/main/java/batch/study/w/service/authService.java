package batch.study.w.service;

import batch.study.w.dto.loginRequestDto;
import batch.study.w.dto.loginResponseDto;

public interface authService {

	loginResponseDto login(loginRequestDto loginRequestDto);
}

