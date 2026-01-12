package batch.study.w.repository;

import batch.study.w.entity.userPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface userPointRepository extends JpaRepository<userPointEntity, Long>, userPointRepositoryDsl {
	
	/**
	 * del_yn이 0인 userPointEntity 목록 조회 (Spring Batch용)
	 */
	List<userPointEntity> findByDelYn(Integer delYn);
}

