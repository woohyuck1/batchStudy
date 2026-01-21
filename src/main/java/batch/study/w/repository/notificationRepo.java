package batch.study.w.repository;

import batch.study.w.entity.notificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface notificationRepo extends JpaRepository<notificationEntity, Long>, notificationRepoDsl {

	
}
