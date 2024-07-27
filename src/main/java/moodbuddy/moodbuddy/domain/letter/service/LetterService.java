package moodbuddy.moodbuddy.domain.letter.service;

import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqDTO;
import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqUpdateDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResDetailsDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResPageDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResSaveDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResUpdateDTO;

import java.util.concurrent.CompletableFuture;

public interface LetterService {
    // 고민상담소 첫 페이지
    LetterResPageDTO letterPage();

    // 쿼디 답장 알람 설정
    LetterResUpdateDTO updateLetterAlarm(LetterReqUpdateDTO letterReqUpdateDTO);

    // 고민 편지 작성
    LetterResSaveDTO letterSave(Long kakaoId, LetterReqDTO letterReqDTO);

    // 고민 편지 내용
    LetterResDetailsDTO letterDetails(Long letterId);

    // 연동한 GPT API로 고민에 대한 답장을 받은 후 저장
    void letterAnswerSave(Long kakaoId, LetterResSaveDTO letterResSaveDTO);
}
