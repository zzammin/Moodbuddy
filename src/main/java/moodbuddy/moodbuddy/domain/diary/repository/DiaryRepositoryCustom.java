package moodbuddy.moodbuddy.domain.diary.repository;

import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindAllDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResFindOneDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DiaryRepositoryCustom {
    DiaryResDraftFindAllDTO draftFindAll(Long userId);
    DiaryResFindOneDTO findOne(Long diaryId);
    Page<DiaryResFindOneDTO> findAllPageable(Long userId, Pageable pageable);
}
