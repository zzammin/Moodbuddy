package moodbuddy.moodbuddy.domain.diary.service;

import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqDraftSaveDTO;
import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqSaveDTO;
import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqUpdateDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;

public interface DiaryService {
    // 일기 저장
    DiaryResSaveDTO save(DiaryReqSaveDTO diaryReqSaveDTO);

    // 일기 수정
    DiaryResUpdateDTO update(DiaryReqUpdateDTO diaryReqUpdateDTO);

    // 일기 삭제
    DiaryResDeleteDTO delete(Long diaryId);

    // 일기 임시 저장
    DiaryResDraftSaveDTO draftSave(DiaryReqDraftSaveDTO diaryResDraftSaveDTO);

    // 일기 임시 저장 날짜 조회
    DiaryResDraftFindAllDTO draftFindAll();

    // 일기 하나 조회
    DiaryResFindOneDTO findOne(Long diaryId);

    // 일기 전체 조회
    DiaryResFindAllDTO findAll();

    // 일기 비슷한 감정 조회
    DiaryResSimilarFindAllDTO similarFindAll(DiaryEmotion diaryEmotion);

    // 검색어 조회

    // 상세검색 조회
}
