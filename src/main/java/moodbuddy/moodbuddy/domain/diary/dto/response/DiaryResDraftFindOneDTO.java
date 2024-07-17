package moodbuddy.moodbuddy.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Builder
public class DiaryResDraftFindOneDTO {
    @Schema(description = "일기 고유 식별자(diaryId)", example = "1")
    private Long diaryId;
    @Schema(description = "사용자 고유 식별자(kakaoId)", example = "2")
    private Long kakaoId;
    @Schema(description = "일기 날짜", example = "2023-07-02T15:30:00")
    private LocalDate diaryDate;
    @Schema(description = "일기 상태(DRAFT, PUBLISHED", example = "DRAFT")
    private DiaryStatus diaryStatus;

    public DiaryResDraftFindOneDTO(Long diaryId, Long kakaoId, LocalDate diaryDate, DiaryStatus diaryStatus) {
        this.diaryId = diaryId;
        this.kakaoId = kakaoId;
        this.diaryDate = diaryDate;
        this.diaryStatus = diaryStatus;
    }
}
