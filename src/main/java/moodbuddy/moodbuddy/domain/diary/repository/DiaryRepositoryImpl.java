package moodbuddy.moodbuddy.domain.diary.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqFilterDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDetailDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindAllDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindOneDTO;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;
import moodbuddy.moodbuddy.domain.diaryImage.entity.DiaryImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static moodbuddy.moodbuddy.domain.diary.entity.QDiary.diary;
import static moodbuddy.moodbuddy.domain.diaryImage.entity.QDiaryImage.diaryImage;
import static org.hibernate.query.results.Builders.fetch;

public class DiaryRepositoryImpl implements DiaryRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public DiaryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public DiaryResDraftFindAllDTO draftFindAllByKakaoId(Long kakaoId) {
        List<DiaryResDraftFindOneDTO> draftList = queryFactory
                .select(Projections.constructor(DiaryResDraftFindOneDTO.class,
                        diary.id,
                        diary.kakaoId,
                        diary.diaryDate,
                        diary.diaryStatus
                ))
                .from(diary)
                .where(diary.kakaoId.eq(kakaoId)
                        .and(diary.diaryStatus.eq(DiaryStatus.DRAFT)))
                .fetch()
                .stream()
                .map(d -> new DiaryResDraftFindOneDTO(d.getProductId(), d.getKakaoId(), d.getDiaryDate(), d.getDiaryStatus()))
                .collect(Collectors.toList());

        return new DiaryResDraftFindAllDTO(draftList);
    }

    @Override
    public DiaryResDetailDTO findOneByDiaryId(Long diaryId) {
        DiaryResDetailDTO diaryResDetailDTO = queryFactory.select(Projections.constructor(DiaryResDetailDTO.class,
                        diary.id,
                        diary.kakaoId,
                        diary.diaryTitle,
                        diary.diaryDate,
                        diary.diaryContent,
                        diary.diaryWeather,
                        diary.diaryEmotion,
                        diary.diaryStatus,
                        diary.diarySummary
                ))
                .from(diary)
                .where(diary.id.eq(diaryId))
                .fetchOne();

        List<String> diaryImgList = queryFactory.select(diaryImage.diaryImgURL)
                .from(diaryImage)
                .where(diaryImage.diary.id.eq(diaryId))
                .fetch();

        diaryResDetailDTO.setDiaryImgList(diaryImgList);

        return diaryResDetailDTO;
    }

    @Override
    public Page<DiaryResDetailDTO> findAllByKakaoIdWithPageable(Long kakaoId, Pageable pageable) {
        List<Diary> diaries = queryFactory.selectFrom(diary)
                .where(diary.kakaoId.eq(kakaoId)
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
                .where(diary.kakaoId.eq(kakaoId))
                .fetchCount();

        return new PageImpl<>(diaryList, pageable, total);
    }

    @Override
    public Page<DiaryResDetailDTO> findAllByEmotionWithPageable(DiaryEmotion emotion, Long kakaoId, Pageable pageable) {
        List<Diary> diaries = queryFactory.selectFrom(diary)
                .where(diaryEmotionEq(emotion).and(diary.kakaoId.eq(kakaoId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Map<Long, List<String>> diaryImages = queryFactory.selectFrom(diaryImage)
                .where(diaryImage.diary.id.in(diaries.stream().map(Diary::getId).collect(Collectors.toList())))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        diaryImage -> diaryImage.getDiary().getId(),
                        Collectors.mapping(DiaryImage::getDiaryImgURL, Collectors.toList())
                ));

        List<DiaryResDetailDTO> dtoList = diaries.stream()
                .map(d -> new DiaryResDetailDTO(
                        d.getId(),
                        d.getKakaoId(),
                        d.getDiaryTitle(),
                        d.getDiaryDate(),
                        d.getDiaryContent(),
                        d.getDiaryWeather(),
                        d.getDiaryEmotion(),
                        d.getDiaryStatus(),
                        d.getDiarySummary(),
                        diaryImages.getOrDefault(d.getId(), List.of())
                ))
                .collect(Collectors.toList());

        long total = queryFactory.selectFrom(diary)
                .where(diaryEmotionEq(emotion).and(diary.kakaoId.eq(kakaoId)))
                .fetchCount();

        return new PageImpl<>(dtoList, pageable, total);
    }

    @Override
    public Page<DiaryResDetailDTO> findAllByFilterWithPageable(DiaryReqFilterDTO filterDTO, Long kakaoId, Pageable pageable) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(diary.kakaoId.eq(kakaoId));

        if (filterDTO.getYear() != null) {
            startDate = LocalDateTime.of(filterDTO.getYear(), 1, 1, 0, 0);
            endDate = startDate.plusYears(1).minusSeconds(1);
            builder.and(betweenDates(startDate, endDate));
        }

        if (filterDTO.getMonth() != null) {
            builder.and(monthMatches(filterDTO.getMonth()));
        }

        if (filterDTO.getKeyWord() != null && !filterDTO.getKeyWord().isEmpty()) {
            builder.and(containsKeyword(filterDTO.getKeyWord()));
        }

        if (filterDTO.getDiaryEmotion() != null) {
            builder.and(diaryEmotionEq(filterDTO.getDiaryEmotion()));
        }


        List<Diary> results = queryFactory.selectFrom(diary)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(diary)
                .where(builder)
                .fetchCount();

        List<Long> diaryIds = results.stream().map(Diary::getId).collect(Collectors.toList());

        Map<Long, List<String>> diaryImagesMap = queryFactory
                .selectFrom(diaryImage)
                .where(diaryImage.diary.id.in(diaryIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        diaryImage -> diaryImage.getDiary().getId(),
                        Collectors.mapping(DiaryImage::getDiaryImgURL, Collectors.toList())
                ));

        List<DiaryResDetailDTO> dtoList = results.stream()
                .map(d -> DiaryResDetailDTO.builder()
                        .diaryId(d.getId())
                        .kakaoId(d.getKakaoId())
                        .diaryTitle(d.getDiaryTitle())
                        .diaryDate(d.getDiaryDate())
                        .diaryContent(d.getDiaryContent())
                        .diaryWeather(d.getDiaryWeather())
                        .diaryEmotion(d.getDiaryEmotion())
                        .diaryStatus(d.getDiaryStatus())
                        .diarySummary(d.getDiarySummary())
                        .diaryImgList(diaryImagesMap.getOrDefault(d.getId(), List.of()))
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return diary.diaryTitle.containsIgnoreCase(keyword)
                .or(diary.diaryContent.containsIgnoreCase(keyword))
                .or(diary.diarySummary.containsIgnoreCase(keyword));
    }

    private BooleanExpression betweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return diary.diaryDate.between(startDate, endDate);
        } else if (startDate != null) {
            return diary.diaryDate.goe(startDate);
        } else if (endDate != null) {
            return diary.diaryDate.loe(endDate);
        } else {
            return null;
        }
    }

    private BooleanExpression monthMatches(Integer month) {
        if (month == null) {
            return null;
        }
        // 날짜의 월을 비교하는 조건 추가
        return diary.diaryDate.month().eq(month);
    }

    private BooleanExpression diaryEmotionEq(DiaryEmotion emotion) {
        return emotion != null ? diary.diaryEmotion.eq(emotion) : null;
    }
}
