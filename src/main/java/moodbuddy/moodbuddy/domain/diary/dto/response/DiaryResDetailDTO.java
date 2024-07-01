package moodbuddy.moodbuddy.domain.diary.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Long diaryId;
    private Long userId;
    private String diaryTitle;
    private LocalDateTime diaryDate;
    private String diaryContent;
    private DiaryWeather diaryWeather;
    private DiaryEmotion diaryEmotion;
    private DiaryStatus diaryStatus;
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
