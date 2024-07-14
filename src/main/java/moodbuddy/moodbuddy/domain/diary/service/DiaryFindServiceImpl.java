package moodbuddy.moodbuddy.domain.diary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.global.common.exception.ErrorCode;
import moodbuddy.moodbuddy.global.common.exception.diary.DiaryNoAccessException;
import moodbuddy.moodbuddy.global.common.exception.diary.DiaryNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static moodbuddy.moodbuddy.global.common.exception.ErrorCode.NOT_FOUND_DIARY;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DiaryFindServiceImpl implements DiaryFindService {
    private final DiaryRepository diaryRepository;

    @Override
    public Diary findDiaryById(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new DiaryNotFoundException(NOT_FOUND_DIARY));
    }

    @Override
    public void validateDiaryAccess(Diary findDiary, Long kakaoId) {
        if (!findDiary.getKakaoId().equals(kakaoId)) {
            throw new DiaryNoAccessException(ErrorCode.NO_ACCESS_DIARY);
        }
    }
}