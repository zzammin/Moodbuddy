package moodbuddy.moodbuddy.domain.diary.service;

import moodbuddy.moodbuddy.domain.diary.dto.request.*;
import moodbuddy.moodbuddy.domain.diary.dto.response.*;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;

import java.io.IOException;
import java.util.Map;

public interface DiaryService {
    // 일기 저장
    DiaryResSaveDTO save(DiaryReqSaveDTO diaryReqSaveDTO) throws IOException;

    // 일기 수정
    DiaryResUpdateDTO update(DiaryReqUpdateDTO diaryReqUpdateDTO) throws IOException;

    // 일기 삭제
    DiaryResDeleteDTO delete(Long diaryId);

    // 일기 임시 저장
    DiaryResDraftSaveDTO draftSave(DiaryReqDraftSaveDTO diaryResDraftSaveDTO);

    // 일기 임시 저장 날짜 조회
    DiaryResDraftFindAllDTO draftFindAll();


    // 일기 임시 저장 선택 삭제
    DiaryResDraftSelectDeleteDTO draftSelectDelete(DiaryReqDraftSelectDeleteDTO diaryReqDraftSelectDeleteDTO);

    // 일기 하나 조회
    DiaryResFindOneDTO findOne(Long diaryId);

    // 일기 전체 조회
    DiaryResFindAllDTO findAll();

    // 일기 비슷한 감정 조회
    DiaryResSimilarFindAllDTO similarFindAll(DiaryEmotion diaryEmotion);

    // 검색어 조회

    // 상세검색 조회

    // 편지지 개수 증가 (일기 작성 시 편지지 개수 증가)
    void letterNumPlus(Long userEmail);

    // 캘린더 달 이동 (캘린더의 < , > 버튼)
    DiaryResCalendarMonthListDTO monthlyCalendar(DiaryReqCalendarMonthDTO calendarMonthDTO);

    // 일기 한 줄 요약 보여주기
    DiaryResCalendarSummaryDTO summary(DiaryReqCalendarSummaryDTO calendarSummaryDTO);

    // 네이버 클라우드 API 연동을 위한 Request Body 생성
    Map<String,Object> getRequestBody(String content);

    // 일기 한 줄 요약
    String summarize(String content);
}
