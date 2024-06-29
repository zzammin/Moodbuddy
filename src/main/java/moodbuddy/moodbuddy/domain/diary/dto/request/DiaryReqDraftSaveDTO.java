package moodbuddy.moodbuddy.domain.diary.dto.request;

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
public class DiaryReqDraftSaveDTO {
    private String diaryTitle;
    private LocalDateTime diaryDate;
    private String diaryContent;
    private DiaryWeather diaryWeather;
    private List<MultipartFile> diaryImgList;
}
