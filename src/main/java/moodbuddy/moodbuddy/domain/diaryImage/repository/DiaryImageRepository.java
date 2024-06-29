package moodbuddy.moodbuddy.domain.diaryImage.repository;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diaryImage.entity.DiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryImageRepository extends JpaRepository<DiaryImage, Long> {
    List<DiaryImage> findByDiaryImgURL(String diaryImgURL);
    Optional<List<DiaryImage>> findByDiary(Diary diary);

}
