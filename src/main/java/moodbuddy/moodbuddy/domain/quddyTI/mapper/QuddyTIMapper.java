package moodbuddy.moodbuddy.domain.quddyTI.mapper;

import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiarySubject;
import moodbuddy.moodbuddy.domain.quddyTI.dto.response.QuddyTIResDetailDTO;
import moodbuddy.moodbuddy.domain.quddyTI.entity.QuddyTI;
import org.modelmapper.ModelMapper;

import java.util.Map;

public class QuddyTIMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static QuddyTIResDetailDTO toQuddyTIResDetailDTO(QuddyTI quddyTI) {
        return modelMapper.map(quddyTI, QuddyTIResDetailDTO.class);
    }

    public static QuddyTI toQuddyTI(Long kakaoId, Map<DiaryEmotion, Long> emotionCounts, Map<DiarySubject, Long> subjectCounts) {
        return QuddyTI.builder()
                .kakaoId(kakaoId)
                .happinessCount(emotionCounts.getOrDefault(DiaryEmotion.HAPPINESS, 0L).intValue())
                .angerCount(emotionCounts.getOrDefault(DiaryEmotion.ANGER, 0L).intValue())
                .disgustCount(emotionCounts.getOrDefault(DiaryEmotion.DISGUST, 0L).intValue())
                .fearCount(emotionCounts.getOrDefault(DiaryEmotion.FEAR, 0L).intValue())
                .neutralCount(emotionCounts.getOrDefault(DiaryEmotion.NEUTRAL, 0L).intValue())
                .sadnessCount(emotionCounts.getOrDefault(DiaryEmotion.SADNESS, 0L).intValue())
                .surpriseCount(emotionCounts.getOrDefault(DiaryEmotion.SURPRISE, 0L).intValue())
                .dailyCount(subjectCounts.getOrDefault(DiarySubject.DAILY, 0L).intValue())
                .growthCount(subjectCounts.getOrDefault(DiarySubject.GROWTH, 0L).intValue())
                .emotionCount(subjectCounts.getOrDefault(DiarySubject.EMOTION, 0L).intValue())
                .travelCount(subjectCounts.getOrDefault(DiarySubject.TRAVEL, 0L).intValue())
                .quddyTIType("monthly")
                .build();
    }
}

