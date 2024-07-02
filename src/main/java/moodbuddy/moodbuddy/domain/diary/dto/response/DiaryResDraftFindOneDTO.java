package moodbuddy.moodbuddy.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "일기 고유 식별자(diaryId)")
    private Long productId;
    @Schema(description = "사용자 고유 식별자(userId)")
    private Long userId;
    @Schema(description = "일기 날짜")
    private LocalDateTime diaryDate;
    @Schema(description = "일기 상태")
    private DiaryStatus diaryStatus;

    public DiaryResDraftFindOneDTO(Long productId, Long userId, LocalDateTime diaryDate, DiaryStatus diaryStatus) {
        this.productId = productId;
        this.userId = userId;
        this.diaryDate = diaryDate;
        this.diaryStatus = diaryStatus;
    }
}
