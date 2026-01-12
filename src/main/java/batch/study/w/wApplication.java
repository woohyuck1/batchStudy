package batch.study.w;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class wApplication {

	public static void main(String[] args) {
		SpringApplication.run(wApplication.class, args);
	}

}
