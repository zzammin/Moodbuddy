package moodbuddy.moodbuddy.domain.diary.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryWeather;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class DiaryResDetailDTO {
    @Schema(description = "일기 고유 식별자(diaryId)")
    private Long diaryId;
    @Schema(description = "사용자 고유 식별자(userId)")
    private Long userId;
    @Schema(description = "일기 제목")
    private String diaryTitle;
    @Schema(description = "일기 날짜")
    private LocalDateTime diaryDate;
    @Schema(description = "일기 내용")
    private String diaryContent;
    @Schema(description = "일기 날씨")
    private DiaryWeather diaryWeather;
    @Schema(description = "일기 감정")
    private DiaryEmotion diaryEmotion;
    @Schema(description = "일기 상태")
    private DiaryStatus diaryStatus;
    @Schema(description = "일기 요약")
    private String diarySummary;

    @JsonInclude(JsonInclude.Include.NON_NULL) // 굳이 필요하지 않은 경우가 있음.
    private List<String> diaryImgList;


    public DiaryResDetailDTO(Long diaryId, Long userId, String diaryTitle, LocalDateTime diaryDate, String diaryContent, DiaryWeather diaryWeather, DiaryEmotion diaryEmotion, DiaryStatus diaryStatus, String diarySummary) {
        this.diaryId = diaryId;
        this.userId = userId;
        this.diaryTitle = diaryTitle;
        this.diaryDate = diaryDate;
        this.diaryContent = diaryContent;
        this.diaryWeather = diaryWeather;
        this.diaryEmotion = diaryEmotion;
        this.diaryStatus = diaryStatus;
        this.diarySummary = diarySummary;
    }
    public DiaryResDetailDTO(Long diaryId, Long userId, String diaryTitle, LocalDateTime diaryDate, String diaryContent, DiaryWeather diaryWeather, DiaryEmotion diaryEmotion, DiaryStatus diaryStatus, String diarySummary, List<String> diaryImgList) {
        this.diaryId = diaryId;
        this.userId = userId;
        this.diaryTitle = diaryTitle;
        this.diaryDate = diaryDate;
        this.diaryContent = diaryContent;
        this.diaryWeather = diaryWeather;
        this.diaryEmotion = diaryEmotion;
        this.diaryStatus = diaryStatus;
        this.diarySummary = diarySummary;
        this.diaryImgList = diaryImgList;
    }
}
