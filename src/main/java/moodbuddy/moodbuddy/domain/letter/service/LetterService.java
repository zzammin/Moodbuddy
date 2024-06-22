package moodbuddy.moodbuddy.domain.letter.service;

import moodbuddy.moodbuddy.domain.letter.dto.LetterRequestDTO;
import moodbuddy.moodbuddy.domain.letter.dto.LetterResponseDTO;

public interface LetterService {
    LetterResponseDTO letterPage(Long userId);

    LetterResponseDTO writeLetter(Long userId, LetterRequestDTO letterRequestDTO);

    LetterResponseDTO details(Long userId, Long letterId);
}
