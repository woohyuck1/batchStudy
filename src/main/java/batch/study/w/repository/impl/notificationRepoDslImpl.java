package batch.study.w.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import batch.study.w.repository.baseRepo;
import batch.study.w.repository.notificationRepoDsl;

public class notificationRepoDslImpl extends baseRepo implements notificationRepoDsl {

    public notificationRepoDslImpl(JPAQueryFactory jpaQueryFactory) {
        super(jpaQueryFactory);
        
    }
}
