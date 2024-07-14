package moodbuddy.moodbuddy.domain.quddyTI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiarySubject;
import moodbuddy.moodbuddy.domain.diary.service.DiaryServiceImpl;
import moodbuddy.moodbuddy.domain.quddyTI.dto.response.QuddyTIResDetailDTO;
import moodbuddy.moodbuddy.domain.quddyTI.entity.QuddyTI;
import moodbuddy.moodbuddy.domain.quddyTI.mapper.QuddyTIMapper;
import moodbuddy.moodbuddy.domain.quddyTI.repository.QuddyTIRepository;
import moodbuddy.moodbuddy.global.common.exception.quddyTI.QuddyTINotFoundException;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static moodbuddy.moodbuddy.global.common.exception.ErrorCode.NOT_FOUND_QUDDYTI;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class QuddyTIServiceImpl implements QuddyTIService {

    private final QuddyTIRepository quddyTIRepository;
    private final DiaryServiceImpl diaryService;

    @Override
    @Transactional
    public void saveQuddyTI(QuddyTI quddyTI) {
        quddyTIRepository.save(quddyTI);
    }

    @Override
    @Transactional
    public void aggregateAndSaveDiaryData() {
        Long kakaoId = JwtUtil.getUserId();
        LocalDateTime[] lastMonthRange = getLastMonthDateTimeRange();

        Map<DiaryEmotion, Long> emotionCounts = getDiaryEmotionCounts(lastMonthRange[0], lastMonthRange[1]);
        Map<DiarySubject, Long> subjectCounts = getDiarySubjectCounts(lastMonthRange[0], lastMonthRange[1]);

        QuddyTI quddyTI = QuddyTIMapper.toQuddyTI(kakaoId, emotionCounts, subjectCounts);
        saveQuddyTI(quddyTI);
    }

    @Override
    public QuddyTIResDetailDTO findAll() {
        Long kakaoId = JwtUtil.getUserId();
        QuddyTI findQuddyTI = getQuddyTI(kakaoId);

        return QuddyTIMapper.toQuddyTIResDetailDTO(findQuddyTI);
    }

    private QuddyTI getQuddyTI(Long kakaoId) {
        return quddyTIRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new QuddyTINotFoundException(NOT_FOUND_QUDDYTI));
    }

    private LocalDateTime[] getLastMonthDateTimeRange() {
        LocalDate now = LocalDate.now();
        YearMonth lastMonth = YearMonth.from(now).minusMonths(1);
        LocalDateTime startOfLastMonth = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfLastMonth = lastMonth.atEndOfMonth().atTime(23, 59, 59);
        return new LocalDateTime[]{startOfLastMonth, endOfLastMonth};
    }

    private Map<DiaryEmotion, Long> getDiaryEmotionCounts(LocalDateTime start, LocalDateTime end) {
        Map<DiaryEmotion, Long> emotionCounts = new EnumMap<>(DiaryEmotion.class);
        for (DiaryEmotion emotion : DiaryEmotion.values()) {
            emotionCounts.put(emotion, diaryService.getDiaryEmotionCount(emotion, start, end));
        }
        return emotionCounts;
    }

    private Map<DiarySubject, Long> getDiarySubjectCounts(LocalDateTime start, LocalDateTime end) {
        Map<DiarySubject, Long> subjectCounts = new EnumMap<>(DiarySubject.class);
        for (DiarySubject subject : DiarySubject.values()) {
            subjectCounts.put(subject, diaryService.getDiarySubjectCount(subject, start, end));
        }
        return subjectCounts;
    }
}
