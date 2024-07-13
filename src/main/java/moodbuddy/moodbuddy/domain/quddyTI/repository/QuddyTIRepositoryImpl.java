package moodbuddy.moodbuddy.domain.quddyTI.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class QuddyTIRepositoryImpl implements QuddyTIRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public QuddyTIRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
