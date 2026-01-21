package batch.study.w.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import batch.study.w.config.jwt.jwtAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * MVC Security 설정
 * 
 * Spring MVC 서블릿 기반 엔드포인트 Security 설정
 * - @EnableWebSecurity: MVC Security 활성화
 * - SecurityFilterChain: MVC 엔드포인트 보호
 * - jwtAuthenticationFilter: OncePerRequestFilter (서블릿 기반)
 *   - SecurityContextHolder 사용 (서블릿 기반)
 */
@Slf4j
@Configuration
@EnableWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class securityConfig {

	private final jwtAuthenticationFilter jwtAuthenticationFilter;

	public securityConfig(jwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	private static final String[] IGNORE_PATTERNS = {
		"/swagger-ui/**",
		"/swagger-ui.html",
		"/swagger-ui/index.html",
		"/v3/api-docs/**",
		"/v3/api-docs",
		"/swagger-resources/**",
		"/swagger-resources",
		"/webjars/**",
		"/api-docs/**",
		"/swagger-config/**",
		"/favicon.ico",
		"/error"
	};

	// 인증 없이 접근 가능한 API 경로
	private static final String[] AUTH_WHITELIST = {
		"/api/users/login",
		"/api/users/signup"
	};

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// 보안 필터 제외
		return web -> web.ignoring()
			.requestMatchers(IGNORE_PATTERNS);
	}
	@Bean(name = "mvcCorsConfigurationSource")
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "userId"));
        // corsConfiguration.setAllowedOrigins(Arrays.asList());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

	@Bean(name = "mvcSecurityFilterChain")
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// JWT 인증 필터 추가
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(web -> web
				.requestMatchers(AUTH_WHITELIST).permitAll()
				.requestMatchers("/reactive/**").permitAll()
				.anyRequest().authenticated()
			);

		return http.build();
	}


}
