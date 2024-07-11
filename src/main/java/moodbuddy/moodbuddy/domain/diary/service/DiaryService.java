package moodbuddy.moodbuddy.domain.diary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import moodbuddy.moodbuddy.domain.diary.dto.request.*;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Map;

public interface DiaryService {
    // 일기 저장
    DiaryResDetailDTO save(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException;

    // 일기 수정
    DiaryResDetailDTO update(DiaryReqUpdateDTO diaryReqUpdateDTO) throws IOException;

    // 일기 삭제
    void delete(Long diaryId);

    // 일기 임시 저장
    DiaryResDetailDTO draftSave(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException;

    // 일기 임시 저장 날짜 조회
    DiaryResDraftFindAllDTO draftFindAll();

    // 일기 임시 저장 선택 삭제
    void draftSelectDelete(DiaryReqDraftSelectDeleteDTO diaryReqDraftSelectDeleteDTO);

    // 일기 하나 조회
    DiaryResDetailDTO findOneByDiaryId(Long diaryId);

    // 일기 전체 조회 (페이징)
    Page<DiaryResDetailDTO> findAllWithPageable(Pageable pageable);

    // 일기 비슷한 감정 조회
    Page<DiaryResDetailDTO> findAllByEmotionWithPageable(DiaryEmotion diaryEmotion, Pageable pageable);

    // 검색어 조회 -> 일라스틱서치 사용할 예정
//    Page<DiaryDocument> searchDiariesByKeyword(String keyword, Pageable pageable);

    // 상세검색 조회
    Page<DiaryResDetailDTO> findAllByFilter(DiaryReqFilterDTO diaryReqFilterDTO, Pageable pageable);

    /** =========================================================  위 정목 아래 재민  ========================================================= **/

    // 이번 달 일기 개수와 편지지 개수 증가
    void numPlus(Long kakaoId);

    // 일기 한 줄 요약
    String summarize(String content);

    // 네이버 클라우드 API 연동을 위한 Request Body 생성
    Map<String,Object> getRequestBody(String content);

    /**
     * =========================================================  위 재민 아래 다연  =========================================================
     **/

    //일기 감정 분석
    DiaryResResponseDto description() throws JsonProcessingException;
}
