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
import moodbuddy.moodbuddy.domain.diary.mapper.DiaryMapper;
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

        String userEmail = JwtUtil.getEmail();
        Diary diary = DiaryMapper.toEntity(diaryReqSaveDTO, userEmail);
        diary = diaryRepository.save(diary);

        if (diaryReqSaveDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqSaveDTO.getDiaryImgList(), diary);
        }

        return DiaryMapper.toSaveDTO(diary);
    }

    @Override
    @Transactional
    public DiaryResUpdateDTO update(DiaryReqUpdateDTO diaryReqUpdateDTO) throws IOException {
        log.info("[DiaryService] update");

        Diary diary = diaryRepository.findById(diaryReqUpdateDTO.getDiaryId()).get(); // 예외 처리 로직 추가

        diary.updateDiary(diaryReqUpdateDTO.getDiaryTitle(), diaryReqUpdateDTO.getDiaryDate(), diaryReqUpdateDTO.getDiaryContent(), diaryReqUpdateDTO.getDiaryWeather());

        if (diaryReqUpdateDTO.getImagesToDelete() != null) {
            diaryImageService.deleteDiaryImages(diaryReqUpdateDTO.getImagesToDelete());
        }

        if (diaryReqUpdateDTO.getDiaryImgList() != null) {
            diaryImageService.saveDiaryImages(diaryReqUpdateDTO.getDiaryImgList(), diary);
        }

        diary = diaryRepository.save(diary);
        return DiaryMapper.toUpdateDTO(diary);
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
    public DiaryResDraftDeleteDTO draftDelete(Long diaryId) {
        return null;
    }

    @Override
    public DiaryResDraftDeleteAllDTO draftDeleteAll() {
        return null;
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
}
