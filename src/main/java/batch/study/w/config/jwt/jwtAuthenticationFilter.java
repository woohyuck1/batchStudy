package batch.study.w.config.jwt;

import batch.study.w.common.exception.ErrorCode;
import batch.study.w.common.response.apiResponse;
import batch.study.w.security.customUserDetails;
import batch.study.w.util.jwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class jwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	private final jwtUtil jwtUtil;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
		throws ServletException, IOException {

		try {
			// 1. Request Header에서 토큰 추출
			String token = resolveToken(request);

			// 2. 토큰이 없는 경우 (인증이 필요한 경로가 아니면 통과)
			if (!StringUtils.hasText(token)) {
				// 인증이 필요한 경로인지 확인은 SecurityFilterChain에서 처리
				filterChain.doFilter(request, response);
				return;
			}

			// 3. 토큰 검증 및 인증 정보 설정
			if (jwtUtil.validateToken(token)) {
				Authentication authentication = getAuthentication(token);
				if (authentication != null) {
					SecurityContextHolder.getContext().setAuthentication(authentication);
					log.debug("JWT 인증 성공: userId = {}", authentication.getName());
				}
			} else {
				// 토큰이 유효하지 않은 경우
				handleJwtException(response);
				return;
			}

			// 4. 다음 필터로 진행
			filterChain.doFilter(request, response);

		} catch (Exception e) {
			log.error("jwt ", e);
			handleJwtException(response);
		}
	}

	/**
	 * Request Header에서 토큰 추출
	 * Authorization: Bearer {token}
	 */
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}
		return null;
	}

	/**
	 * JWT 토큰에서 인증 정보 생성
	 */
	private Authentication getAuthentication(String token) {
		// 토큰에서 사용자 정보 추출
		Long userSeq = jwtUtil.getUserIdFromToken(token);
		String userId = jwtUtil.getUserIdFromTokenAsString(token);

		// CustomUserDetails 생성
		customUserDetails userDetails = new customUserDetails(
			userSeq,
			userId,
			null, // userName은 토큰에 없으므로 null (필요시 토큰에 추가)
			null  // password는 필요 없음
		);

		// 인증 객체 생성
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			userDetails,  // principal (CustomUserDetails)
			null,         // credentials (비밀번호는 필요 없음)
			userDetails.getAuthorities() // 권한
		);

		return authentication;
	}

	/**
	 * JWT 예외 처리 및 응답 (401 Unauthorized)
	 */
	private void handleJwtException(HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		apiResponse<Void> errorResponse = apiResponse.error(ErrorCode.UNAUTHORIZED.getMessage());
		String jsonResponse = objectMapper.writeValueAsString(errorResponse);

		response.getWriter().write(jsonResponse);
		response.getWriter().flush();
	}
}

