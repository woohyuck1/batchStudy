package batch.study.w.util;

import batch.study.w.common.exception.BusinessException;
import batch.study.w.common.exception.ErrorCode;
import batch.study.w.config.jwt.jwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class jwtUtil {
    
	private final jwtProperties jwtProperties;

	public String generateToken(Long userSeq, String userId) {
		try {
			Date now = new Date();
			Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

			return Jwts.builder()
				.subject(String.valueOf(userSeq))
				.claim("userId", userId)
				.issuedAt(now)
				.expiration(expiryDate)
				.signWith(getSigningKey())
				.compact();
		} catch (IllegalArgumentException e) {
			log.error("JWT 토큰 생성 중 오류 발생: ", e);
			throw new BusinessException(ErrorCode.UNAUTHORIZED);
		} catch (Exception e) {
			log.error("JWT 토큰 생성 중 예상치 못한 오류 발생: ", e);
			throw new BusinessException(ErrorCode.UNAUTHORIZED);
		}
	}

	public Long getUserIdFromToken(String token) {
		try {
			return Long.parseLong(
				Jwts.parser()
					.verifyWith(getSigningKey())
					.build()
					.parseSignedClaims(token)
					.getPayload()
					.getSubject()
			);
		} catch (Exception e) {
			log.error("JWT 토큰 파싱  ", e);
			throw new BusinessException(ErrorCode.UNAUTHORIZED);
		}
	}

	public String getUserIdFromTokenAsString(String token) {
		try {
			return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("userId", String.class);
		} catch (Exception e) {
			log.error("JWT 토큰 파싱  ", e);
			throw new BusinessException(ErrorCode.UNAUTHORIZED);
		}
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private SecretKey getSigningKey() {
		try {
			return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			log.error("JWT 시크릿 키", e);
			throw new BusinessException(ErrorCode.UNAUTHORIZED);
		}
	}
}


