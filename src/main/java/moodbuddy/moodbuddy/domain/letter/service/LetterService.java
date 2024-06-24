package moodbuddy.moodbuddy.domain.letter.service;

import moodbuddy.moodbuddy.domain.letter.dto.LetterReqDTO;
import moodbuddy.moodbuddy.domain.letter.dto.LetterResDetailsDTO;
import moodbuddy.moodbuddy.domain.letter.dto.LetterResPageDTO;

public interface LetterService {
    LetterResPageDTO letterPage();

    void writeLetter(LetterReqDTO letterRequestDTO);

    LetterResDetailsDTO details(Long letterId);
}
