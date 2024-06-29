package moodbuddy.moodbuddy.domain.diary.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindAllDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindOneDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResFindOneDTO;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static moodbuddy.moodbuddy.domain.diary.entity.QDiary.diary;
import static moodbuddy.moodbuddy.domain.diaryImage.entity.QDiaryImage.diaryImage;

public class DiaryRepositoryImpl implements DiaryRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public DiaryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public DiaryResDraftFindAllDTO draftFindAllByUserId(Long userId) {
        List<DiaryResDraftFindOneDTO> draftList = queryFactory
                .select(Projections.constructor(DiaryResDraftFindOneDTO.class,
                        diary.id,
                        diary.diaryDate,
                        diary.diaryStatus,
                        diary.userId
                ))
                .from(diary)
                .where(diary.userId.eq(userId)
                        .and(diary.diaryStatus.eq(DiaryStatus.DRAFT)))
                .fetch()
                .stream()
                .map(d -> new DiaryResDraftFindOneDTO(d.getId(), d.getDiaryDate(), d.getDiaryStatus(), d.getUserEmail()))
                .collect(Collectors.toList());

        return new DiaryResDraftFindAllDTO(draftList);
    }

    @Override
    public DiaryResFindOneDTO findOneByDiaryId(Long diaryId) {
        DiaryResFindOneDTO diaryResFindOne = queryFactory.select(Projections.constructor(DiaryResFindOneDTO.class,
                        diary.id,
                        diary.diaryTitle,
                        diary.diaryDate,
                        diary.diaryContent,
                        diary.diaryWeather,
                        diary.diaryEmotion,
                        diary.diaryStatus,
                        diary.userId
                ))
                .from(diary)
                .where(diary.id.eq(diaryId))
                .fetchOne();

        List<String> diaryImgList = queryFactory.select(diaryImage.diaryImgURL)
                .from(diaryImage)
                .where(diaryImage.diary.id.eq(diaryId))
                .fetch();

        diaryResFindOne.setDiaryImgList(diaryImgList);

        return diaryResFindOne;
    }

    @Override
    public Page<DiaryResFindOneDTO> findAllByUserIdWithPageable(Long userId, Pageable pageable) {
        List<Diary> diaries = queryFactory.selectFrom(diary)
                .where(diary.userId.eq(userId)
                        .and(diary.diaryStatus.eq(DiaryStatus.PUBLISHED)))
                .orderBy(pageable.getSort().stream()
                        .map(order -> new OrderSpecifier(
                                order.isAscending() ? Order.ASC : Order.DESC,
                                new PathBuilder<>(diary.getType(), diary.getMetadata())
                                        .get(order.getProperty())))
                        .toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<DiaryResFindOneDTO> diaryList = diaries.stream().map(d -> {
            List<String> diaryImgList = queryFactory.select(diaryImage.diaryImgURL)
                    .from(diaryImage)
                    .where(diaryImage.diary.id.eq(d.getId()))
                    .fetch();

            return DiaryResFindOneDTO.builder()
                    .diaryId(d.getId())
                    .diaryTitle(d.getDiaryTitle())
                    .diaryDate(d.getDiaryDate())
                    .diaryContent(d.getDiaryContent())
                    .diaryWeather(d.getDiaryWeather())
                    .diaryEmotion(d.getDiaryEmotion())
                    .diaryStatus(d.getDiaryStatus())
                    .userId(d.getUserId())
                    .diaryImgList(diaryImgList)
                    .build();
        }).collect(Collectors.toList());

        long total = queryFactory.selectFrom(diary)
                .where(diary.userId.eq(userId))
                .fetchCount();

        return new PageImpl<>(diaryList, pageable, total);
    }
}
