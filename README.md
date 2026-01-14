##프로젝트 개요

Spring Batch를 활용한 대용량 데이터 처리 시스템입니다. 
매일 자정에 모든 사용자의 포인트를 자동으로 증가시키는 스케줄 배치 작업을 구현하고, 
Excel 파일을 통한 대량 사용자 등록, Kafka를 활용한 에러 로그 수집 등 
실무에서 필요한 다양한 기능을 포함한 백엔드 애플리케이션입니다.

주요 기능

1. 배치 처리
- 스케줄 배치: 매일 자정(00:00:00) 자동 실행되는 포인트 증가 배치
- 수동 실행: API를 통한 배치 작업 수동 실행 지원
- 중복 실행 방지: JobParameters 기반 동일 날짜 중복 실행 방지
- 재처리 안전장치: `increment_dt` 필드를 통한 중복 포인트 증가 방지

2. 대량 데이터 처리
- Excel 일괄 등록: Excel 파일 업로드를 통한 대량 사용자 등록
- JDBC Batch Insert: 개별 INSERT 대신 배치 처리로 성능 최적화
- 자동 헤더 인식: Excel 헤더 자동 감지 및 컬럼 매핑

3. 에러 처리 및 모니터링
- Kafka 기반 에러 로그 수집: 비동기 에러 로그 전송
- AOP 기반 에러 추적: 서비스 레이어 전역 에러 로깅
- Global Exception Handler: 통일된 예외 처리
- CloudEvent 형식: 표준화된 이벤트 형식으로 로그 전송

 4. API 문서화
- Swagger/OpenAPI: REST API 자동 문서화

기술 스택

Backend
- Java 21
- Spring Boot 3.5.0
- Spring Batch: 배치 작업 처리
- Spring Data JPA: ORM 및 데이터 접근
- QueryDSL 5.1.0: 타입 안전한 동적 쿼리
- Spring Security: 인증 및 권한 관리
- JWT (jjwt 0.12.3): 토큰 기반 인증

Database
- MariaDB: 관계형 데이터베이스
- JDBC Template: 고성능 배치 INSERT 처리


아키텍처 및 설계 특징



2. 배치 아키텍처
	Job : 하나의 비즈니스 배치 프로세스
    Step : Job을 구성하는 하나의 작업 단위 job 하나에 여러 step이 있을 수 있음
    Tasklet : Step을 구성하는 작업 단위
    Chunk : Reader가 null 될 때까지 반복 + N건마다 commit  chunk(100) = 100건을 하나의 트랜잭션으로 처리
    commit : db에저장

3. 에러 처리 아키텍처
Service Layer (AOP) → Kafka Producer → Kafka Topic → Kafka Consumer → Database

학습 및 적용 내용

- Spring Batch를 활용한 스케줄 배치 구현
- 대량 데이터 처리 최적화 (JDBC Batch Insert)
- Kafka를 활용한 비동기 메시지 처리
- AOP를 통한 횡단 관심사 분리
- QueryDSL을 활용한 타입 안전 쿼리 작성
- Excel 파일 처리 및 대량 등록 기능
- Docker Compose를 활용한 인프라 구성

향후 개선 사항

- 배치 실패 시 자동 재시도 로직 추가
- Redis를 활용한 캐싱 전략
- CI/CD 파이프라인 구축

Java 21, Spring Boot 3.5.0, MariaDB, Kafka
