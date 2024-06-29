package moodbuddy.moodbuddy.domain.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryResDraftFindOneDTO {
    private Long id;
    private LocalDateTime diaryDate;
    private DiaryStatus diaryStatus;
    private String userEmail;
}
