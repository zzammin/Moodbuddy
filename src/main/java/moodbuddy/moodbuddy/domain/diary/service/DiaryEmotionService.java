package moodbuddy.moodbuddy.domain.diary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDTO;

public interface DiaryEmotionService {
    //일기 감정 분석
    DiaryResDTO description() throws JsonProcessingException;
}
