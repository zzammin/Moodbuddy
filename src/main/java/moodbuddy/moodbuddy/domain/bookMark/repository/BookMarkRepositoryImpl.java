package moodbuddy.moodbuddy.domain.bookMark.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDetailDTO;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;
import moodbuddy.moodbuddy.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static moodbuddy.moodbuddy.domain.bookMark.entity.QBookMark.bookMark;
import static moodbuddy.moodbuddy.domain.diary.entity.QDiary.diary;
import static moodbuddy.moodbuddy.domain.diaryImage.entity.QDiaryImage.diaryImage;

public class BookMarkRepositoryImpl implements BookMarkRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public BookMarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<DiaryResDetailDTO> bookMarkFindAllWithPageable(User user, Pageable pageable) {
        List<Diary> diaries = queryFactory.selectFrom(diary)
                .join(bookMark).on(diary.id.eq(bookMark.diary.id))
                .where(bookMark.user.eq(user)
                        .and(diary.diaryStatus.eq(DiaryStatus.PUBLISHED)))
                .orderBy(pageable.getSort().stream()
                        .map(order -> new OrderSpecifier(
                                order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                                new PathBuilder<>(diary.getType(), diary.getMetadata())
                                        .get(order.getProperty())))
                        .toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<DiaryResDetailDTO> diaryList = diaries.stream().map(d -> {
            List<String> diaryImgList = queryFactory.select(diaryImage.diaryImgURL)
                    .from(diaryImage)
                    .where(diaryImage.diary.id.eq(d.getId()))
                    .fetch();

            return DiaryResDetailDTO.builder()
                    .diaryId(d.getId())
                    .diaryTitle(d.getDiaryTitle())
                    .diaryDate(d.getDiaryDate())
                    .diaryContent(d.getDiaryContent())
                    .diaryWeather(d.getDiaryWeather())
                    .diaryEmotion(d.getDiaryEmotion())
                    .diaryStatus(d.getDiaryStatus())
                    .diarySummary(d.getDiarySummary())
                    .kakaoId(d.getKakaoId())
                    .diaryImgList(diaryImgList)
                    .build();
        }).collect(Collectors.toList());

        long total = queryFactory.selectFrom(diary)
                .join(bookMark).on(diary.id.eq(bookMark.diary.id))
                .where(bookMark.user.eq(user))
                .fetchCount();

        return new PageImpl<>(diaryList, pageable, total);
    }
}
