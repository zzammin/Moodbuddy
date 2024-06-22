package moodbuddy.moodbuddy.domain.diary.dto.response;

import lombok.Data;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import java.time.LocalDateTime;

@Data
public class DiaryResSaveDTO {
    private Long diaryId;
    private String userEmail;
    private LocalDateTime diaryDate;
    private DiaryEmotion diaryEmotion;
}
