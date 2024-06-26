package moodbuddy.moodbuddy.domain.letter.service;

import moodbuddy.moodbuddy.domain.letter.dto.request.LetterReqDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResDetailsDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResPageDTO;
import moodbuddy.moodbuddy.domain.letter.dto.response.LetterResSaveDTO;

public interface LetterService {
    LetterResPageDTO letterPage();

    LetterResSaveDTO save(LetterReqDTO letterReqDTO);

    LetterResDetailsDTO details(Long letterId);
}
