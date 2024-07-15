package moodbuddy.moodbuddy.domain.diary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import moodbuddy.moodbuddy.domain.diary.dto.request.*;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiarySubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public interface DiaryService {
    // 일기 저장
    DiaryResDetailDTO save(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException;

    // 일기 수정
    DiaryResDetailDTO update(DiaryReqUpdateDTO diaryReqUpdateDTO) throws IOException;

    // 일기 삭제
    void delete(Long diaryId) throws IOException;

    // 일기 임시 저장
    DiaryResDetailDTO draftSave(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException;

    // 일기 임시 저장 날짜 조회
    DiaryResDraftFindAllDTO draftFindAll();

    // 일기 임시 저장 선택 삭제
    void draftSelectDelete(DiaryReqDraftSelectDeleteDTO diaryReqDraftSelectDeleteDTO);

    // 일기 하나 조회
    DiaryResDetailDTO findOneByDiaryId(Long diaryId);

    // 일기 전체 조회 (페이징)
    Page<DiaryResDetailDTO> findAll(Pageable pageable);

    // 일기 비슷한 감정 조회
    Page<DiaryResDetailDTO> findAllByEmotion(DiaryEmotion diaryEmotion, Pageable pageable);

    // 상세검색 조회
    Page<DiaryResDetailDTO> findAllByFilter(DiaryReqFilterDTO diaryReqFilterDTO, Pageable pageable);
}
