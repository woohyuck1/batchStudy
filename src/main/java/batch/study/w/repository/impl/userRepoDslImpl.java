package batch.study.w.repository.impl;

import batch.study.w.dto.userDto;
import batch.study.w.repository.userRepoDsl;

import com.querydsl.core.types.QBean;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static batch.study.w.entity.QuserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class userRepoDslImpl implements userRepoDsl {

	private final JPAQueryFactory queryFactory;

	QBean<userDto> USERCONSTRUCTOR = Projections.bean(
		userDto.class,
		userEntity.userId,
		userEntity.userName,
		userEntity.creDt,
		userEntity.delYn
	);

	@Override
	public List<userDto> findAllUsers() {
		return queryFactory
			.select(USERCONSTRUCTOR)
			.from(userEntity)
			.where(userEntity.delYn.eq(0))
			.fetch();
	}
}

