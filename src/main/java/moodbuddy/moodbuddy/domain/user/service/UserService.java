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

    // 캘린더 달 이동 (캘린더의 < , > 버튼)
    UserResCalendarMonthListDTO monthlyCalendar(UserReqCalendarMonthDTO calendarMonthDTO);

    // 일기 한 줄 요약 보여주기
    UserResCalendarSummaryDTO summary(UserReqCalendarSummaryDTO calendarSummaryDTO);

    // 한 달이 지나면 이번 달 일기 개수를 저번 달 일기 개수로 변경하고, 이번 달 일기 개수 초기화하기
    void changeDiaryNums();

    //월별 통계 보기
    UserResStatisticsMonthDTO getMonthStatic(LocalDate month);

    //내 활동 _ 일기 횟수 조회 , 년 + 해당하는 월
    List<DiaryNumsDto> getDiaryNums(LocalDate year);

    //연별 감정 횟수 조회
    List<EmotionStaticDto> getEmotionNums();

    //프로필 조회
    UserProfileDto getUserProfile();

    //프로필 수정
    UserProfileDto updateProfile(UserProfileUpdateDto dto) throws IOException;

//    // 사용자가 설정한 알림 시간에 문자 보내기
//    void scheduleUserMessage(Long kakaoId);

    // 다음 달 나에게 짧은 한 마디
    UserResMonthCommentDTO monthComment(UserReqMonthCommentDTO userReqMonthCommentDTO);

    // 다음 달 나에게 짧은 한 마디 수정
    UserResMonthCommentUpdateDTO monthCommentUpdate(UserReqMonthCommentUpdateDTO userReqMonthCommentUpdateDTO);

    // kakaoId를 통한 사용자 찾기
    User findUserByKakaoId(Long kakaoId);

    // 이번 달 일기 개수와 편지지 개수 변경
    void changeCount(Long kakaoId, boolean increment);

     void setUserCheckTodayDairy(Long kakaoId, Boolean check);

    /** 테스트를 위한 임시 자체 로그인 **/
    LoginResponseDto login(UserReqLoginDTO userReqLoginDTO);

    /** 오늘 일기 작성한 지 가능 여부 **/
    UserResCheckTodayDiaryDTO checkTodayDiary();

}
