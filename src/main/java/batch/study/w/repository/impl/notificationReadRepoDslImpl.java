package batch.study.w.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import batch.study.w.repository.baseRepo;
import batch.study.w.repository.notificationReadRepoDsl;

public class notificationReadRepoDslImpl extends baseRepo implements notificationReadRepoDsl {

    public notificationReadRepoDslImpl(JPAQueryFactory jpaQueryFactory) {
        super(jpaQueryFactory);
        //TODO Auto-generated constructor stub
    }
    
}
