package batch.study.w.config.jwt;

import batch.study.w.common.exception.ErrorCode;
import batch.study.w.common.response.apiResponse;
import batch.study.w.security.jwtAuthenticationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

	private final jwtAuthenticationService jwtAuthenticationService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
		throws ServletException, IOException {

		try {
			// Request Header에서 토큰 추출
			String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
			String token = jwtAuthenticationService.extractToken(bearerToken);

			if (!StringUtils.hasText(token)) {
				filterChain.doFilter(request, response);
				return;
			}

			Authentication authentication = jwtAuthenticationService.getAuthentication(token);
			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.debug("JWT 인증 성공");
			} else {
				log.debug("JWT 토큰 검증 실패");
				handleJwtException(response);
				return;
			}

			filterChain.doFilter(request, response);

		} catch (Exception e) {
			log.error("jwt ", e);
			handleJwtException(response);
		}
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

