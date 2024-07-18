package moodbuddy.moodbuddy.domain.diary.util;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;
import moodbuddy.moodbuddy.domain.diary.repository.DiaryRepository;
import moodbuddy.moodbuddy.domain.diaryImage.entity.DiaryImage;
import moodbuddy.moodbuddy.domain.diaryImage.service.DiaryImageService;
import moodbuddy.moodbuddy.global.common.exception.ErrorCode;
import moodbuddy.moodbuddy.global.common.exception.diary.DiaryTodayExistingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DiaryUtil {

    public static void validateExistingDiary(DiaryRepository diaryRepository, LocalDate diaryDate, Long kakaoId) {
        if (diaryRepository.findByDiaryDateAndKakaoIdAndDiaryStatus(diaryDate, kakaoId, DiaryStatus.PUBLISHED).isPresent()) {
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
        // null 체크 및 빈 리스트로 초기화
        if (existingImgUrls == null) {
            existingImgUrls = new ArrayList<>();
        }

        List<DiaryImage> diaryImages = diaryImageService.findImagesByDiary(diary);
        for (DiaryImage diaryImage : diaryImages) {
            String imageUrl = diaryImage.getDiaryImgURL().trim();
            boolean shouldDelete = true;
            for (String existingUrl : existingImgUrls) {
                System.out.println("Comparing: [" + imageUrl + "] with [" + existingUrl.trim() + "]");
                if (imageUrl.equals(existingUrl.trim())) {
                    shouldDelete = false;
                    break;
                }
            }
            if (shouldDelete) {
                System.out.println("===============================");
                System.out.println("Existing Image URL: " + existingImgUrls);
                System.out.println("diaryImage.getDiaryImgURL() = " + diaryImage.getDiaryImgURL());
                diaryImageService.deleteDiaryImage(diaryImage);
            }
        }
    }

}