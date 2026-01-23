package batch.study.w.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class errorLogProducer {

	private final KafkaTemplate<String, CloudEvent> kafkaTemplate;
	private final ObjectMapper objectMapper;
	private static final String TOPIC = "error-logs";

	public void sendLog(Map<String, Object> logData) {
		try {
			// Map을 JSON 문자열로 변환
			String jsonData = objectMapper.writeValueAsString(logData);

			// CloudEvent 생성
			CloudEvent cloudEvent = CloudEventBuilder.v1()
				.withId(java.util.UUID.randomUUID().toString())
				.withType("error.log")
				.withSource(URI.create("batch-study-service"))
				.withData("application/json", jsonData.getBytes())
				.build();

			// Kafka로 전송
			kafkaTemplate.send(TOPIC, cloudEvent);

			log.debug("Error log sent to Kafka topic: {}", TOPIC);

		} catch (Exception e) {
			log.error("Failed to send error log to Kafka", e);
		}
	}
}
