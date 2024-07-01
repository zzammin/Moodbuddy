package moodbuddy.moodbuddy.domain.diary.repository;

import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqFilterDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDetailDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindAllDTO;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DiaryRepositoryCustom {
    DiaryResDraftFindAllDTO draftFindAllByUserId(Long userId);
    DiaryResDetailDTO findOneByDiaryId(Long diaryId);
    Page<DiaryResDetailDTO> findAllByUserIdWithPageable(Long userId, Pageable pageable);
    Page<DiaryResDetailDTO> findAllByEmotionWithPageable(DiaryEmotion emotion, Long userId, Pageable pageable);
    Page<DiaryResDetailDTO> findAllByFilterWithPageable(DiaryReqFilterDTO filterDTO, Long userId, Pageable pageable);
}
