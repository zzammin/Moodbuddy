package moodbuddy.moodbuddy.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryWeather;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryReqUpdateDTO {
    @Schema(description = "수정할 일기 고유 식별자(diaryId)", example = "1")
    private Long diaryId;
    @Schema(description = "수정할 일기 제목", example = "쿼카의 하카")
    private String diaryTitle;
    @Schema(description = "수정할 일기 날짜", example = "2023-07-02T15:30:00")
    private LocalDateTime diaryDate;
    @Schema(description = "수정할 일기 내용", example = "쿼카쿼카쿼카쿼카쿼카쿼카")
    private String diaryContent;
    @Schema(description = "수정할 일기 날씨(CLEAR, CLOUDY, RAIN, SNOW)", example = "CLEAR")
    private DiaryWeather diaryWeather;
    @Schema(description = "수정할 일기 상태(DRAFT, PUBLISHED)", example = "DRAFT")
    private DiaryStatus diaryStatus;
    @Schema(description = "수정할 일기 이미지 List", example = "[\"image1.png\", \"image2.png\"]")
    private List<MultipartFile> diaryImgList;
//    @Schema(description = "삭제할 일기 이미지 List", example = "[\"이미지 URL\", \"이미지 URL\"]")
//    private List<String> imagesToDelete;
}
