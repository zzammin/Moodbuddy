package moodbuddy.moodbuddy.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class DiaryResCalendarMonthDTO {
    @Schema(description = "일기 작성 날짜")
    private LocalDateTime diaryDate;
    @Schema(description = "일기의 감정")
    private DiaryEmotion diaryEmotion;
}
