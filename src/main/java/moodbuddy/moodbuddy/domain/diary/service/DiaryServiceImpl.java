package moodbuddy.moodbuddy.domain.diary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqDraftSaveDTO;
import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqSaveDTO;
import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqUpdateDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DiaryServiceImpl implements DiaryService {
    private final DiaryRepository diaryRepository;
    @Override
    @Transactional
    public DiaryResSaveDTO save(DiaryReqSaveDTO diaryReqSaveDTO) {
        return new DiaryResSaveDTO();
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
}
