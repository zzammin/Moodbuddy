package moodbuddy.moodbuddy.domain.user.service;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqCalendarMonthDTO;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarMonthListDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResMainPageDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    // 메인 화면 이동
    UserResMainPageDTO mainPage();

    // 각 emotion의 횟수를 세는 메소드
    Map<DiaryEmotion,Integer> emotionNum(List<Diary> diaryList);

    // emotion 횟수의 최댓값을 찾기 위한 메소드
    Map<DiaryEmotion, Integer> getMaxEmotion(Map<DiaryEmotion, Integer> emotionNum);

    // 캘린더 달 이동 (캘린더의 < , > 버튼)
    UserResCalendarMonthListDTO monthlyCalendar(UserReqCalendarMonthDTO calendarMonthDTO);

    // 일기 한 줄 요약 보여주기
    UserResCalendarSummaryDTO summary(UserReqCalendarSummaryDTO calendarSummaryDTO);

    // 한 달이 지나면 이번 달 일기 개수를 저번 달 일기 개수로 변경하기
    void changeDiaryNums();
}
