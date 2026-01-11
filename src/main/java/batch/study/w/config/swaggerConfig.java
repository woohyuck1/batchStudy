package batch.study.w.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class swaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.title("Batch Study API")
			.version("v1.0")
			.description("Batch Study 프로젝트 API 문서")
			.contact(new Contact()
				.name("Batch Study Team")
				.email("contact@example.com"));

		// JWT 인증을 위한 Security Scheme 설정
		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name("Authorization");

		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList("bearerAuth");

		return new OpenAPI()
			.info(info)
			.components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
			.addSecurityItem(securityRequirement);
	}
}

