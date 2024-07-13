package moodbuddy.moodbuddy.domain.diary.service;

import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiarySubject;

import java.time.LocalDateTime;

public interface DiaryCountService {
    long countByEmotionAndDateRange(DiaryEmotion diaryEmotion, LocalDateTime start, LocalDateTime end);
    long countBySubjectAndDateRange(DiarySubject subject, LocalDateTime start, LocalDateTime end);
}
