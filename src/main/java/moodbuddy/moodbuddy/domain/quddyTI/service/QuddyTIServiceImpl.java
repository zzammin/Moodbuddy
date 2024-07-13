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
        LocalDate now = LocalDate.now();
        YearMonth lastMonth = YearMonth.from(now).minusMonths(1);
        LocalDateTime startOfLastMonth = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfLastMonth = lastMonth.atEndOfMonth().atTime(23, 59, 59);

        long happinessCount = diaryService.getDiaryEmotionCount(DiaryEmotion.HAPPINESS, startOfLastMonth, endOfLastMonth);
        long angerCount = diaryService.getDiaryEmotionCount(DiaryEmotion.ANGER, startOfLastMonth, endOfLastMonth);
        long disgustCount = diaryService.getDiaryEmotionCount(DiaryEmotion.DISGUST, startOfLastMonth, endOfLastMonth);
        long fearCount = diaryService.getDiaryEmotionCount(DiaryEmotion.FEAR, startOfLastMonth, endOfLastMonth);
        long neutralCount = diaryService.getDiaryEmotionCount(DiaryEmotion.NEUTRAL, startOfLastMonth, endOfLastMonth);
        long sadnessCount = diaryService.getDiaryEmotionCount(DiaryEmotion.SADNESS, startOfLastMonth, endOfLastMonth);
        long surpriseCount = diaryService.getDiaryEmotionCount(DiaryEmotion.SURPRISE, startOfLastMonth, endOfLastMonth);

        long dailyCount = diaryService.getDiarySubjectCount(DiarySubject.DAILY, startOfLastMonth, endOfLastMonth);
        long growthCount = diaryService.getDiarySubjectCount(DiarySubject.GROWTH, startOfLastMonth, endOfLastMonth);
        long emotionCount = diaryService.getDiarySubjectCount(DiarySubject.EMOTION, startOfLastMonth, endOfLastMonth);
        long travelCount = diaryService.getDiarySubjectCount(DiarySubject.TRAVEL, startOfLastMonth, endOfLastMonth);

        QuddyTI quddyTI = QuddyTI.builder()
                .kakaoId(kakaoId)
                .happinessCount((int) happinessCount)
                .angerCount((int) angerCount)
                .disgustCount((int) disgustCount)
                .fearCount((int) fearCount)
                .neutralCount((int) neutralCount)
                .sadnessCount((int) sadnessCount)
                .surpriseCount((int) surpriseCount)
                .dailyCount((int) dailyCount)
                .growthCount((int) growthCount)
                .emotionCount((int) emotionCount)
                .travelCount((int) travelCount)
                .quddyTIType("monthly") // 필요에 따라 적절한 타입을 설정하세요.
                .build();

        saveQuddyTI(quddyTI);
    }

    @Override
    public QuddyTIResDetailDTO findAll() {
        Long kakaoId = JwtUtil.getUserId();
        QuddyTI findQuddyTI = getQuddyTI(kakaoId);

        return QuddyTIMapper.toQuddyTIResDetailDTO(findQuddyTI);
    }

    private QuddyTI getQuddyTI(Long kakaoId) {
        Optional<QuddyTI> optionalQuddyTI = quddyTIRepository.findByKakaoId(kakaoId);
        if(!optionalQuddyTI.isPresent()) {
            throw new QuddyTINotFoundException(NOT_FOUND_QUDDYTI);
        }
        return optionalQuddyTI.get();
    }
}
