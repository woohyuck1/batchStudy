package batch.study.w.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class poiConfig {

	@PostConstruct
	public void configureZipSecureFile() {

		ZipSecureFile.setMinInflateRatio(0.001);
		
		ZipSecureFile.setMaxEntrySize(1024 * 1024 * 1024L);
		
		log.info("POI");
	}
}

