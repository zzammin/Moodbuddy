package moodbuddy.moodbuddy.domain.letter.service;

import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResDetailsDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResPageDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResSaveDTO;

import java.time.LocalDateTime;

public interface LetterService {
    // 고민상담소 첫 페이지
    LetterResPageDTO letterPage();

    // 고민 편지 작성
    LetterResSaveDTO save(LetterReqDTO letterReqDTO);

    // 고민 편지 내용
    LetterResDetailsDTO details(Long letterId);

    // 연동한 gpt api로 고민에 대한 답장을 받은 후 저장
    void answerSave(String worryContent, Integer format, LocalDateTime letterDate);

    // 알림톡 보내기 (또는 알림)
    void alarmTalk(String fcmRegistration);
}
