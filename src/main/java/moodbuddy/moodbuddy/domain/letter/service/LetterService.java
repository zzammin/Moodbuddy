package moodbuddy.moodbuddy.domain.letter.service;

import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResDetailsDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResPageDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResSaveDTO;

public interface LetterService {
    // 고민상담소 첫 페이지
    LetterResPageDTO letterPage();

    // 고민 편지 작성
    LetterResSaveDTO save(LetterReqDTO letterReqDTO);

    // 고민 편지 내용
    LetterResDetailsDTO details(Long letterId);

    // 고민 답장 내용 저장
    void answerSave(String worryContent);

    // 알림톡 보내기 (또는 알림)
    void alarmTalk();
}
