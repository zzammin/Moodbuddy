package moodbuddy.moodbuddy.domain.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Builder
public class DiaryResDraftFindOneDTO {
    private Long productId;
    private Long userId;
    private LocalDateTime diaryDate;
    private DiaryStatus diaryStatus;

    public DiaryResDraftFindOneDTO(Long productId, Long userId, LocalDateTime diaryDate, DiaryStatus diaryStatus) {
        this.productId = productId;
        this.userId = userId;
        this.diaryDate = diaryDate;
        this.diaryStatus = diaryStatus;
    }
}
