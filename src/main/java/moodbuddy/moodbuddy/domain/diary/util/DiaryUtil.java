package moodbuddy.moodbuddy.domain.diary.util;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.diaryImage.entity.DiaryImage;
import moodbuddy.moodbuddy.domain.diaryImage.service.DiaryImageService;
import moodbuddy.moodbuddy.global.common.exception.ErrorCode;
import moodbuddy.moodbuddy.global.common.exception.diary.DiaryTodayExistingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DiaryUtil {

    public static void validateExistingDiary(DiaryRepository diaryRepository, LocalDateTime diaryDate, Long kakaoId) {
        if (diaryRepository.findByDiaryDateAndKakaoId(diaryDate, kakaoId).isPresent()) {
            throw new DiaryTodayExistingException(ErrorCode.TODAY_EXISTING_DIARY);
        }
    }

    public static void saveDiaryImages(DiaryImageService diaryImageService, List<MultipartFile> diaryImgList, Diary diary) throws IOException {
        if (diaryImgList != null) {
            diaryImageService.saveDiaryImages(diaryImgList, diary);
        }
    }

    public static void deleteDiaryImages(DiaryImageService diaryImageService, List<String> imagesToDelete) throws IOException {
        if (imagesToDelete != null) {
            diaryImageService.deleteDiaryImages(imagesToDelete);
        }
    }

    public static void deleteDiaryImages(DiaryImageService diaryImageService, Diary diary) throws IOException {
        List<DiaryImage> images = diaryImageService.findImagesByDiary(diary);
        List<String> imageUrls = images.stream()
                .map(DiaryImage::getDiaryImgURL)
                .collect(Collectors.toList());

        if (!imageUrls.isEmpty()) {
            diaryImageService.deleteDiaryImages(imageUrls);
        }
    }
}