package moodbuddy.moodbuddy.domain.diary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiarySubject;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class DiaryCountServiceImpl implements DiaryCountService {
    private final DiaryRepository diaryRepository;

    @Override
    public long getDiaryEmotionCount(DiaryEmotion diaryEmotion, LocalDateTime start, LocalDateTime end) {
        return diaryRepository.countByEmotionAndDateRange(diaryEmotion, start, end);
    }

    @Override
    public long getDiarySubjectCount(DiarySubject subject, LocalDateTime start, LocalDateTime end) {
        return diaryRepository.countBySubjectAndDateRange(subject, start, end);
    }
}
