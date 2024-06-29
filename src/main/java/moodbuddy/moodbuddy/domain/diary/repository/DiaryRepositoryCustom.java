package moodbuddy.moodbuddy.domain.diary.repository;

import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqFilterDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindAllDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResFindOneDTO;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryEmotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DiaryRepositoryCustom {
    DiaryResDraftFindAllDTO draftFindAllByUserId(Long userId);
    DiaryResFindOneDTO findOneByDiaryId(Long diaryId);
    Page<DiaryResFindOneDTO> findAllByUserIdWithPageable(Long userId, Pageable pageable);
    Page<DiaryResFindOneDTO> findAllByEmotionWithPageable(DiaryEmotion emotion, Long userId, Pageable pageable);
    Page<DiaryResFindOneDTO> findAllByFilterWithPageable(DiaryReqFilterDTO filterDTO, Long userId, Pageable pageable);
}
