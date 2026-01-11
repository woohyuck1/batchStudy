package batch.study.w.repository;

import batch.study.w.entity.userEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface userRepository extends JpaRepository<userEntity, Long>, userRepositoryDsl {

	Optional<userEntity> findByUserIdAndDelYn(String userId, Integer delYn);
}
