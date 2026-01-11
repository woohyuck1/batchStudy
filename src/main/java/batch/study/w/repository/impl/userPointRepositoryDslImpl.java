package batch.study.w.repository.impl;

import batch.study.w.repository.userPointRepositoryDsl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import static batch.study.w.entity.QuserPointEntity.userPointEntity;

@Repository
@RequiredArgsConstructor
public class userPointRepositoryDslImpl implements userPointRepositoryDsl {

	private final JPAQueryFactory queryFactory;

}

