package batch.study.w.sse;

import batch.study.w.dto.messageDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class messageSink {

	// multicast() : 
	// - 하나의 메시지를 여러 구독자에게 동시에 전달
	// - 이미 구독 중인 subscriber만 수신
	private final Sinks.Many<messageDto> sink =
		Sinks.many().multicast().onBackpressureBuffer();
	 // - onBackpressureBuffer = 구독자가 메시지를 처리하는 속도보다
        //   메시지가 더 빨리 들어오면
        // - 일단 메모리 버퍼에 쌓아둠
        // - SSE처럼 느린 클라이언트가 있을 때 씀
	public void send(messageDto message) {
		sink.tryEmitNext(message);
	}

	public Flux<messageDto> asFlux() {
		return sink.asFlux();
	}
}
