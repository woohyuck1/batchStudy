package batch.study.w.service;

import batch.study.w.security.customUserDetails;
import batch.study.w.util.jwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class jwtAuthenticationService {

	private static final String BEARER_PREFIX = "Bearer ";

	private final jwtUtil jwtUtil;

	// Authorization 헤더에서 토큰 추출
	
	public String extractToken(String authorizationHeader) {
		if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX)) {
			return authorizationHeader.substring(BEARER_PREFIX.length());
		}
		return null;
	}

	//JWT 토큰에서 인증 정보 생성
	public Authentication getAuthentication(String token) {
		if (!StringUtils.hasText(token) || !jwtUtil.validateToken(token)) {
			return null;
		}

		// 토큰에서 사용자 정보 추출
		Long userSeq = jwtUtil.getUserIdFromToken(token);
		String userId = jwtUtil.getUserIdFromTokenAsString(token);
		String roles = jwtUtil.getRolesFromToken(token);

		// CustomUserDetails 생성
		customUserDetails userDetails = new customUserDetails(
			userSeq,
			userId,
			null,
			null,
			roles
		);

		// 인증 객체 생성
		return new UsernamePasswordAuthenticationToken(
			userDetails,
			null,
			userDetails.getAuthorities()
		);
	}
}
