package moodbuddy.moodbuddy.domain.diary.repository;

import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindAllDTO;
import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResFindOneDTO;

public interface DiaryRepositoryCustom {
    DiaryResDraftFindAllDTO draftFindAll(String userEmail);
    DiaryResFindOneDTO findOne(Long diaryId);
}
