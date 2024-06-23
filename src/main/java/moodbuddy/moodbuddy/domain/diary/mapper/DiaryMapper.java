package moodbuddy.moodbuddy.domain.diary.mapper;

import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqSaveDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResSaveDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResUpdateDTO;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;
import org.modelmapper.ModelMapper;

public class DiaryMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static Diary toEntity(DiaryReqSaveDTO diaryReqSaveDTO, String userEmail) {
        return Diary.builder()
                .diaryTitle(diaryReqSaveDTO.getDiaryTitle())
                .diaryDate(diaryReqSaveDTO.getDiaryDate())
                .diaryContent(diaryReqSaveDTO.getDiaryContent())
                .diaryWeather(diaryReqSaveDTO.getDiaryWeather())
                .diaryEmotion(DiaryEmotion.HAPPY) // 감정 분석 로직 필요
                .diaryStatus(DiaryStatus.PUBLISHED)
                .diarySummary("summary") // 문장 요약 로직 필요
                .userEmail(userEmail)
                .build();
    }

    public static DiaryResSaveDTO toSaveDTO(Diary diary) {
        return modelMapper.map(diary, DiaryResSaveDTO.class);
    }
    public static DiaryResUpdateDTO toUpdateDTO(Diary diary) {
        return modelMapper.map(diary, DiaryResUpdateDTO.class);
    }
}
