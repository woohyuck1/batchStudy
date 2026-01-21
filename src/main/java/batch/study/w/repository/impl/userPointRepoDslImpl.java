package batch.study.w.repository.impl;

import batch.study.w.repository.userPointRepoDsl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import static batch.study.w.entity.QuserPointEntity.userPointEntity;

@Repository
@RequiredArgsConstructor
public class userPointRepoDslImpl implements userPointRepoDsl {

	private final JPAQueryFactory queryFactory;

}

