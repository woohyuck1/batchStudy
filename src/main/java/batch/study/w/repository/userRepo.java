package batch.study.w.repository;

import batch.study.w.entity.userEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface userRepo extends JpaRepository<userEntity, Long>, userRepoDsl {

	Optional<userEntity> findByUserIdAndDelYn(String userId, Integer delYn);
}
