package moodbuddy.moodbuddy.domain.diary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryReqEmotionDTO {
    private DiaryEmotion diaryEmotion;
}
