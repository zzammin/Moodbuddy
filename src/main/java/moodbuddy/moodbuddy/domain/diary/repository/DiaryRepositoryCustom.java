package moodbuddy.moodbuddy.domain.diary.repository;

import moodbuddy.moodbuddy.domain.diary.dto.response.DiaryResDraftFindAllDTO;

public interface DiaryRepositoryCustom {
    DiaryResDraftFindAllDTO draftFindAll(String userEmail);
}
