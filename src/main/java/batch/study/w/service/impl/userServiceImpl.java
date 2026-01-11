package batch.study.w.service.impl;

import batch.study.w.common.exception.BusinessException;
import batch.study.w.common.exception.ErrorCode;
import batch.study.w.dto.loginRequestDto;
import batch.study.w.dto.loginResponseDto;
import batch.study.w.dto.userDto;
import batch.study.w.dto.userRequestDto;
import batch.study.w.entity.userEntity;
import batch.study.w.entity.userPointEntity;
import batch.study.w.repository.userPointRepository;
import batch.study.w.repository.userRepository;
import batch.study.w.service.userService;
import batch.study.w.util.jwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class userServiceImpl implements userService {

	private final userRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final jwtUtil jwtUtil;
	private final userPointRepository userPointRepository;

	@Override
	@Transactional(readOnly = true)
	public List<userDto> findAllUsers() {
		return userRepository.findAllUsers();
	}

	@Override
	@Transactional
	public userDto save(userRequestDto userRequestDto) {
		String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());

		userEntity newUser = userEntity.builder()
			.userId(userRequestDto.getUserId())
			.userName(userRequestDto.getUserName())
			.password(encodedPassword)
			.build();

		userEntity savedEntity = userRepository.save(newUser);
		long userSeq = savedEntity.getUserSeq();

		userPointEntity userPoint = userPointEntity.builder()
			.userSeq(userSeq)
			.point(0)
			.build();

		userPointRepository.save(userPoint);

		return userDto.from(savedEntity);
	}

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

