package batch.study.w.repository.impl;

import batch.study.w.repository.baseRepo;
import batch.study.w.repository.userPointRepoDsl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import static batch.study.w.entity.QuserPointEntity.userPointEntity;


public class userPointRepoDslImpl extends baseRepo implements userPointRepoDsl {

	public userPointRepoDslImpl(JPAQueryFactory jpaQueryFactory) {
		super(jpaQueryFactory);
		
	}


}

