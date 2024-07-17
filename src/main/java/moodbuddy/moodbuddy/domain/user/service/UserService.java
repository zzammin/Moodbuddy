package moodbuddy.moodbuddy.domain.user.service;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.user.dto.request.*;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarMonthListDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResCalendarSummaryDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.UserResMainPageDTO;
import moodbuddy.moodbuddy.domain.user.dto.response.*;
import moodbuddy.moodbuddy.domain.user.entity.User;

import java.io.IOException;
import java.time.LocalDate;
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

    // 한 달이 지나면 이번 달 일기 개수를 저번 달 일기 개수로 변경하고, 이번 달 일기 개수 초기화하기
    void changeDiaryNums();

    //월별 통계 보기 _ 월별 감정 통계
    List<EmotionStaticDto> getEmotionStatic(LocalDate month);

    //내 활동 _ 일기 횟수 조회 , 년 + 해당하는 월
    List<DiaryNumsDto> getDiaryNums(LocalDate year);

    //연별 감정 횟수 조회
    List<EmotionStaticDto> getEmotionNums();

    //프로필 조회
    UserProfileDto getUserProfile();

    //프로필 수정
    UserProfileDto updateProfile(UserProfileUpdateDto dto) throws IOException;

    // 알람 설정한 유저 리스트 보내기
    List<User> getAllUsersWithAlarms();

    // 사용자 token 받아서 update
    UserResUpdateTokenDTO updateToken(UserReqUpdateTokenDTO userReqTokenDTO);

    // kakaoId를 통한 사용자 찾기
    User findUserByKakaoId(Long kakaoId);

    // 이번 달 일기 개수와 편지지 개수 증가
    void numPlus(Long kakaoId);
}
