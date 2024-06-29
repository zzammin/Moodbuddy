package moodbuddy.moodbuddy.domain.diaryImage.service;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diaryImage.entity.DiaryImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DiaryImageService {
    void saveDiaryImages(List<MultipartFile> diaryImgList, Diary diary) throws IOException;
    void deleteDiaryImages(List<String> imagesToDelete) throws IOException;
    List<DiaryImage> findImagesByDiary(Diary diary);
}

