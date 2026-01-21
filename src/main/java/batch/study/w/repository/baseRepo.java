package batch.study.w.repository;

import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class baseRepo {  
    protected final JPAQueryFactory jpaQueryFactory;

}
