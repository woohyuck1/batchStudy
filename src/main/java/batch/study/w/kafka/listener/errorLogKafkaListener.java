package batch.study.w.kafka.listener;

import batch.study.w.dto.errorLogDto;
import batch.study.w.entity.errorLogEntity;
import batch.study.w.repository.errorLogRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class errorLogKafkaListener {

	private final errorLogRepo errorLogRepository;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = "error-logs", groupId = "${spring.kafka.consumer.group-id}")
	public void onMessage(ConsumerRecord<String, CloudEvent> data, @Nullable Acknowledgment acknowledgment) {

		try {
			log.info("Received error log message from topic: {}, partition: {}, offset: {}", 
				data.topic(), data.partition(), data.offset());
			log.debug("Message key: {}, value: {}", data.key(), data.value());

			CloudEvent cloudEvent = data.value();
			
			String dataString = new String(cloudEvent.getData().toBytes());
			errorLogDto errorLogDto = objectMapper.readValue(dataString, errorLogDto.class);

			// DTO를 Entity로 변환
			errorLogEntity errorLog = errorLogEntity.builder()
				.level(errorLogDto.getLevel())
				.serviceName(errorLogDto.getServiceName())
				.endpoint(errorLogDto.getEndpoint())
				.requestData(errorLogDto.getRequestData())
				.errorMessage(errorLogDto.getErrorMessage())
				.stackTrace(errorLogDto.getStackTrace())
				.requestId(errorLogDto.getRequestId())
				.build();

			// DB에 저장
			errorLogRepository.save(errorLog);

			log.info("Error log saved successfully. ErrorLogId: {}", errorLog.getErrorLogId());

			// 수동 커밋: offset 커밋하여 같은 메시지 다시 읽지 않도록 함
			
		} catch (Exception e) {
			
		}finally {
			acknowledgment.acknowledge();
		}
	}
}
