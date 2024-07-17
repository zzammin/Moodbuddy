package moodbuddy.moodbuddy.domain.quddyTI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiarySubject;
import moodbuddy.moodbuddy.domain.diary.service.DiaryCountService;
import moodbuddy.moodbuddy.domain.quddyTI.dto.response.QuddyTIResDetailDTO;
import moodbuddy.moodbuddy.domain.quddyTI.entity.QuddyTI;
import moodbuddy.moodbuddy.domain.quddyTI.mapper.QuddyTIMapper;
import moodbuddy.moodbuddy.domain.quddyTI.repository.QuddyTIRepository;
import moodbuddy.moodbuddy.global.common.exception.ErrorCode;
import moodbuddy.moodbuddy.global.common.exception.quddyTI.QuddyTINotFoundException;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.EnumMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class QuddyTIServiceImpl implements QuddyTIService {

    private final QuddyTIRepository quddyTIRepository;
    private final DiaryCountService diaryCountService;

    @Override
    @Transactional
    public void saveQuddyTI(QuddyTI quddyTI) {
        quddyTIRepository.save(quddyTI);
    }

    @Override
    @Transactional
    public void aggregateAndSaveDiaryData() {
        final Long kakaoId = JwtUtil.getUserId();
        LocalDate[] lastMonthRange = getLastMonthDateTimeRange();

        Map<DiaryEmotion, Long> emotionCounts = getDiaryEmotionCounts(lastMonthRange[0], lastMonthRange[1]);
        Map<DiarySubject, Long> subjectCounts = getDiarySubjectCounts(lastMonthRange[0], lastMonthRange[1]);

        final String quddyTIType = determineQuddyTIType(emotionCounts, subjectCounts);

        final QuddyTI quddyTI = QuddyTIMapper.toQuddyTI(kakaoId, emotionCounts, subjectCounts, quddyTIType);
        saveQuddyTI(quddyTI);
    }

    @Override
    public QuddyTIResDetailDTO findAll() {
        final Long kakaoId = JwtUtil.getUserId();
        final QuddyTI findQuddyTI = getQuddyTI(kakaoId);
        return QuddyTIMapper.toQuddyTIResDetailDTO(findQuddyTI);
    }

    private QuddyTI getQuddyTI(Long kakaoId) {
        return quddyTIRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new QuddyTINotFoundException(ErrorCode.NOT_FOUND_QUDDYTI));
    }

    private LocalDate[] getLastMonthDateTimeRange() {
        LocalDate now = LocalDate.now();
        YearMonth lastMonth = YearMonth.from(now).minusMonths(1);
        LocalDate startOfLastMonth = lastMonth.atDay(1);
        LocalDate endOfLastMonth = lastMonth.atEndOfMonth();
        return new LocalDate[]{startOfLastMonth, endOfLastMonth};
    }

    private Map<DiaryEmotion, Long> getDiaryEmotionCounts(LocalDate start, LocalDate end) {
        Map<DiaryEmotion, Long> emotionCounts = new EnumMap<>(DiaryEmotion.class);
        for (DiaryEmotion emotion : DiaryEmotion.values()) {
            emotionCounts.put(emotion, diaryCountService.getDiaryEmotionCount(emotion, start, end));
        }
        return emotionCounts;
    }

    private Map<DiarySubject, Long> getDiarySubjectCounts(LocalDate start, LocalDate end) {
        Map<DiarySubject, Long> subjectCounts = new EnumMap<>(DiarySubject.class);
        for (DiarySubject subject : DiarySubject.values()) {
            subjectCounts.put(subject, diaryCountService.getDiarySubjectCount(subject, start, end));
        }
        return subjectCounts;
    }

    private String getEmotionAbbreviation(DiaryEmotion emotion) {
        return switch (emotion) {
            case HAPPINESS -> "H";
            case ANGER -> "A";
            case DISGUST -> "D";
            case FEAR -> "F";
            case NEUTRAL -> "N";
            case SADNESS -> "Sa";
            case SURPRISE -> "Su";
        };
    }

    private String determineQuddyTIType(Map<DiaryEmotion, Long> emotionCounts, Map<DiarySubject, Long> subjectCounts) {
        long totalDiaryCount = emotionCounts.values().stream().mapToLong(Long::longValue).sum();
        String diaryType = totalDiaryCount >= 15 ? "J" : "P";

        String mostFrequentSubject = subjectCounts.entrySet().stream()
                .sorted(Map.Entry.<DiarySubject, Long>comparingByValue().reversed()
                        .thenComparing(entry -> entry.getKey().name()))
                .map(entry -> entry.getKey().name().substring(0, 1))
                .findFirst()
                .orElse("D");

        String mostFrequentEmotion = emotionCounts.entrySet().stream()
                .sorted(Map.Entry.<DiaryEmotion, Long>comparingByValue().reversed()
                        .thenComparing(entry -> entry.getKey().name()))
                .map(entry -> getEmotionAbbreviation(entry.getKey()))
                .findFirst()
                .orElse("H");

        return diaryType + mostFrequentSubject + mostFrequentEmotion;
    }
}