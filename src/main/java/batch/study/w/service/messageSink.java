package batch.study.w.service;

import batch.study.w.dto.messageDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class messageSink {

	private final Sinks.Many<messageDto> sink =
		Sinks.many().multicast().onBackpressureBuffer();

	public void send(messageDto message) {
		sink.tryEmitNext(message);
	}
}
