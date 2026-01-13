package batch.study.w.repository;

import batch.study.w.entity.errorLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface errorLogRepo extends JpaRepository<errorLogEntity, Long> {
}
