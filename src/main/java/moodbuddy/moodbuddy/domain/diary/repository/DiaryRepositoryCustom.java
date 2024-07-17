package moodbuddy.moodbuddy.domain.diary.repository;

import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqFilterDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDetailDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindAllDTO;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import moodbuddy.moodbuddy.domain.diary.entity.DiarySubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;


public interface DiaryRepositoryCustom {
    DiaryResDraftFindAllDTO draftFindAllByKakaoId(Long kakaoId);
    DiaryResDetailDTO findOneByDiaryId(Long diaryId);
    Page<DiaryResDetailDTO> findAllByKakaoIdWithPageable(Long kakaoId, Pageable pageable);
    Page<DiaryResDetailDTO> findAllByEmotionWithPageable(DiaryEmotion emotion, Long kakaoId, Pageable pageable);
    Page<DiaryResDetailDTO> findAllByFilterWithPageable(DiaryReqFilterDTO filterDTO, Long kakaoId, Pageable pageable);
    long countByEmotionAndDateRange(DiaryEmotion emotion, LocalDate start, LocalDate end);
    long countBySubjectAndDateRange(DiarySubject subject, LocalDate start, LocalDate end);
}
