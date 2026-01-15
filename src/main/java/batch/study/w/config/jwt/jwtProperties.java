package batch.study.w.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class jwtProperties {

	private String secret = "n24567abcdefghG@xZ2ffH6beJ1ar&41";
	private Long expiration = 86400000L; // 밀리초 단위
}


