package moodbuddy.moodbuddy.domain.diary.service;

import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiarySubject;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DiaryCountService {
    // 감정 갯수 검색
    long getDiaryEmotionCount(DiaryEmotion diaryEmotion, LocalDate start, LocalDate end);

    // 주제 갯수 검색
    long getDiarySubjectCount(DiarySubject subject, LocalDate start, LocalDate end);
}
