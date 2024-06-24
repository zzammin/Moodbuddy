package moodbuddy.moodbuddy.domain.diary.dto.response;

import lombok.Builder;
import lombok.Data;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import java.time.LocalDateTime;

@Data
@Builder
public class DiaryResSaveDTO {
    private Long diaryId;
    private String userEmail;
    private LocalDateTime diaryDate;
    private DiaryEmotion diaryEmotion;
}
