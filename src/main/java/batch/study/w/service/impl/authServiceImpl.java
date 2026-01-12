package batch.study.w.service.impl;

import batch.study.w.common.exception.BusinessException;
import batch.study.w.common.exception.ErrorCode;
import batch.study.w.dto.loginRequestDto;
import batch.study.w.dto.loginResponseDto;
import batch.study.w.entity.userEntity;
import batch.study.w.repository.userRepo;
import batch.study.w.service.authService;
import batch.study.w.util.jwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class authServiceImpl implements authService {

	private final userRepo userRepository;
	private final PasswordEncoder passwordEncoder;
	private final jwtUtil jwtUtil;

	@Override
	@Transactional(readOnly = true)
	public loginResponseDto login(loginRequestDto loginRequestDto) {
		// 사용자 조회 (삭제되지 않은 사용자만)
		userEntity user = userRepository.findByUserIdAndDelYn(loginRequestDto.getUserId(), 0)
			.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

		return createLoginResponse(user, loginRequestDto);
	}

	private loginResponseDto createLoginResponse(userEntity user, loginRequestDto loginRequestDto) {
		// 비밀번호 검증
		if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
			throw new BusinessException(ErrorCode.LOGIN_FAILED);
		}

		// JWT 토큰 생성
		String accessToken = jwtUtil.generateToken(user.getUserSeq(), user.getUserId());

		// 응답 DTO 생성 
		return loginResponseDto.builder()
			.accessToken(accessToken)
			.tokenType("Bearer")
			.userSeq(user.getUserSeq())
			.userId(user.getUserId())
			.userName(user.getUserName())
			.build();
	}
}

