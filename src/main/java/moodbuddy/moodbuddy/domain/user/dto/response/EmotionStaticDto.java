package moodbuddy.moodbuddy.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EmotionStaticDto {
    @Schema(description = "일기의 감정")
    private DiaryEmotion diaryEmotion;
    @Schema(description = "감정 횟수")
    private Integer nums;
}
