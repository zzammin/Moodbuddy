package moodbuddy.moodbuddy.domain.diary.dto.request;

import moodbuddy.moodbuddy.domain.diary.entity.DiaryWeather;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class DiaryReqDraftSaveDTO {
    private String diaryTitle;
    private LocalDateTime diaryDate;
    private String diaryContent;
    private DiaryWeather diaryWeather;
    private List<MultipartFile> diaryImgList;
}
