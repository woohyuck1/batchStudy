package batch.study.w.common;

import batch.study.w.service.errorLogProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class errorLogAspect {

	private final errorLogProducer logProducer;

	@Around("execution(* batch.study.w.service..*.*(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		String service = joinPoint.getTarget().getClass().getSimpleName();
		String endpoint = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
		
		log.debug("AOP 적용 - Service: {}, Method: {}", service, endpoint);
		//	서비스단에서 바로 처리하면 서비스 처리 지연이 발생함 -> 사용자가 불편함을 느낄지도
		// 카프카는 비동기 처리  
		// userSeq도 추카해서 하면 좋겠당
		try {
			
			return joinPoint.proceed(); // 실제 메서드 호출
		} catch (Exception e) {
			log.error("Service 에러 발생 - Service: {}, Method: {}, Error: {}", service, endpoint, e.getMessage());
			logProducer.sendLog(Map.of(
				"level", "ERROR",
				"serviceName", service,
				"endpoint", endpoint,
				"errorMessage", e.getMessage() != null ? e.getMessage() : "Unknown error",
				"timestamp", Instant.now().toString()
			));
			throw e;
		}
	}
}
