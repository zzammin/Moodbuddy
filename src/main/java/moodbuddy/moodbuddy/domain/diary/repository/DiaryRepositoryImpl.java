package moodbuddy.moodbuddy.domain.diary.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindAllDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindOneDTO;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;

import java.util.List;
import java.util.stream.Collectors;

import static moodbuddy.moodbuddy.domain.diary.entity.QDiary.diary;

public class DiaryRepositoryImpl implements DiaryRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public DiaryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public DiaryResDraftFindAllDTO draftFindAll(String userEmail) {
        List<DiaryResDraftFindOneDTO> draftList = queryFactory
                .select(Projections.constructor(DiaryResDraftFindOneDTO.class,
                        diary.id,
                        diary.diaryDate,
                        diary.diaryStatus,
                        diary.userEmail
                ))
                .from(diary)
                .where(diary.userEmail.eq(userEmail)
                        .and(diary.diaryStatus.eq(DiaryStatus.DRAFT)))
                .fetch()
                .stream()
                .map(d -> new DiaryResDraftFindOneDTO(d.getId(), d.getDiaryDate(), d.getDiaryStatus(), d.getUserEmail()))
                .collect(Collectors.toList());

        return new DiaryResDraftFindAllDTO(draftList);
    }
}
