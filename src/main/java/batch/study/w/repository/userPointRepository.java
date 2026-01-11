package batch.study.w.repository;

import batch.study.w.entity.userPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userPointRepository extends JpaRepository<userPointEntity, Long>, userPointRepositoryDsl {
}

