package moodbuddy.moodbuddy.domain.diary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqDraftSaveDTO;
import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqSaveDTO;
import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqUpdateDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.diaryImage.service.DiaryImageServiceImpl;
import moodbuddy.moodbuddy.global.common.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DiaryServiceImpl implements DiaryService {
    private final ModelMapper modelMapper;
    private final DiaryRepository diaryRepository;
    private final DiaryImageServiceImpl diaryImageService;

    @Override
    @Transactional
    public DiaryResSaveDTO save(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException {
        log.info("[DiaryService] save");

        Diary diary = createDiaryFromDTO(diaryReqSaveDTO); // DiaryReqSaveDTO -> Diary
        diary = diaryRepository.save(diary); // diary 저장
        diaryImageService.saveDiaryImages(diaryReqSaveDTO.getDiaryImgList(), diary); // 이미지 저장 로직 시행

        return modelMapper.map(diary, DiaryResSaveDTO.class); // mapper 후 리턴
    }

    @Override
    @Transactional
    public DiaryResUpdateDTO update(DiaryReqUpdateDTO diaryReqUpdateDTO) {
        return new DiaryResUpdateDTO();
    }

    @Override
    @Transactional
    public DiaryResDeleteDTO delete(Long diaryId) {
        return new DiaryResDeleteDTO();
    }

    @Override
    @Transactional
    public DiaryResDraftSaveDTO draftSave(DiaryReqDraftSaveDTO diaryResDraftDTO) {
        return new DiaryResDraftSaveDTO();
    }

    @Override
    public DiaryResDraftFindAllDTO draftFindAll() {
        return new DiaryResDraftFindAllDTO();
    }

    @Override
    public DiaryResFindOneDTO findOne(Long diaryId) {
        return new DiaryResFindOneDTO();
    }

    @Override
    public DiaryResFindAllDTO findAll() {
        return new DiaryResFindAllDTO();
    }

    @Override
    public DiaryResSimilarFindAllDTO similarFindAll(DiaryEmotion diaryEmotion) {
        return new DiaryResSimilarFindAllDTO();
    }

    /** 추가 메서드 **/
    private Diary createDiaryFromDTO(DiaryReqSaveDTO diaryReqSaveDTO) {
        return Diary.builder()
                .diaryTitle(diaryReqSaveDTO.getDiaryTitle())
                .diaryDate(diaryReqSaveDTO.getDiaryDate())
                .diaryContent(diaryReqSaveDTO.getDiaryContent())
                .diaryWeather(diaryReqSaveDTO.getDiaryWeather())
                .diaryEmotion(DiaryEmotion.HAPPY) // 감정 분석 로직 필요
                .diaryStatus(DiaryStatus.PUBLISHED) // 문장 요약 로직 필요
                .diarySummary("summary")
                .userEmail(JwtUtil.getEmail())
                .build();
    }
}
