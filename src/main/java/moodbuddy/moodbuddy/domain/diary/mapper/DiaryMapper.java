package moodbuddy.moodbuddy.domain.diary.mapper;

import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqSaveDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;
import org.modelmapper.ModelMapper;

public class DiaryMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static Diary toDiaryEntity(DiaryReqSaveDTO diaryReqSaveDTO, Long userId, String summary) {
        return Diary.builder()
                .diaryTitle(diaryReqSaveDTO.getDiaryTitle())
                .diaryDate(diaryReqSaveDTO.getDiaryDate())
                .diaryContent(diaryReqSaveDTO.getDiaryContent())
                .diaryWeather(diaryReqSaveDTO.getDiaryWeather())
                .diaryEmotion(DiaryEmotion.HAPPY) // 감정 분석 로직 필요
                .diaryStatus(DiaryStatus.PUBLISHED)
                .diarySummary(summary) // 문장 요약 로직 필요
                .userId(userId)
                .build();
    }

    public static Diary toDraftEntity(DiaryReqSaveDTO diaryReqSaveDTO, Long userId) {
        return Diary.builder()
                .diaryTitle(diaryReqSaveDTO.getDiaryTitle())
                .diaryDate(diaryReqSaveDTO.getDiaryDate())
                .diaryContent(diaryReqSaveDTO.getDiaryContent())
                .diaryWeather(diaryReqSaveDTO.getDiaryWeather())
                .diaryStatus(DiaryStatus.DRAFT)
                .userId(userId)
                .build();
    }

    public static DiaryResDetailDTO toDetailDTO(Diary diary) {
        return modelMapper.map(diary, DiaryResDetailDTO.class);
    }

}
