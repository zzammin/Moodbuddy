package moodbuddy.moodbuddy.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryWeather;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryReqUpdateDTO {
    @Schema(description = "수정할 일기 고유 식별자(diaryId)")
    private Long diaryId;
    @Schema(description = "수정할 일기 제목")
    private String diaryTitle;
    @Schema(description = "수정할 일기 날짜")
    private LocalDateTime diaryDate;
    @Schema(description = "수정할 일기 내용")
    private String diaryContent;
    @Schema(description = "수정할 일기 날씨(CLEAR, CLOUDY, RAIN, SNOW)")
    private DiaryWeather diaryWeather;
    @Schema(description = "수정할 일기 이미지 List")
    private List<MultipartFile> diaryImgList;
    @Schema(description = "삭제할 일기 이미지 List")
    private List<String> imagesToDelete;
}
