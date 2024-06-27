package moodbuddy.moodbuddy.domain.user.service;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
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
}
