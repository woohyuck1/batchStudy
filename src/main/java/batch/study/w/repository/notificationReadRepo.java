package batch.study.w.repository;

import batch.study.w.entity.notificationReadEntity;
import batch.study.w.entity.notificationReadId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface notificationReadRepo extends JpaRepository<notificationReadEntity, notificationReadId> {

}
