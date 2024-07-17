package moodbuddy.moodbuddy.domain.diary.util;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.diaryImage.entity.DiaryImage;
import moodbuddy.moodbuddy.domain.diaryImage.service.DiaryImageService;
import moodbuddy.moodbuddy.global.common.exception.ErrorCode;
import moodbuddy.moodbuddy.global.common.exception.diary.DiaryTodayExistingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DiaryUtil {

    public static void validateExistingDiary(DiaryRepository diaryRepository, LocalDate diaryDate, Long kakaoId) {
        if (diaryRepository.findByDiaryDateAndKakaoId(diaryDate, kakaoId).isPresent()) {
            throw new DiaryTodayExistingException(ErrorCode.TODAY_EXISTING_DIARY);
        }
    }

    public static void saveDiaryImages(DiaryImageService diaryImageService, List<MultipartFile> diaryImgList, Diary diary) throws IOException {
        if (diaryImgList != null && !diaryImgList.isEmpty()) {
            diaryImageService.saveDiaryImages(diaryImgList, diary);
        }
    }

    public static void deleteAllDiaryImages(DiaryImageService diaryImageService, Diary diary) throws IOException {
        diaryImageService.deleteAllDiaryImages(diary);
    }
    public static void deleteExcludingImages(DiaryImageService diaryImageService, Diary diary, List<String> existingImgUrls) throws IOException {
        List<DiaryImage> diaryImages = diaryImageService.findImagesByDiary(diary);
        for (DiaryImage diaryImage : diaryImages) {
            if (!existingImgUrls.contains(diaryImage.getDiaryImgURL())) {
                diaryImageService.deleteDiaryImage(diaryImage);
            }
        }
    }
}