package moodbuddy.moodbuddy.domain.diaryImage.service;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diaryImage.entity.DiaryImage;
import moodbuddy.moodbuddy.domain.profileImage.entity.ProfileImage;
import moodbuddy.moodbuddy.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DiaryImageService {
    void saveDiaryImages(List<MultipartFile> diaryImgList, Diary diary) throws IOException;
    void deleteAllDiaryImages(Diary diary) throws IOException;
    List<DiaryImage> findImagesByDiary(Diary diary);
    String saveProfileImages(MultipartFile newProfileImg) throws IOException;
    void deleteDiaryImage(DiaryImage diaryImage);
}

