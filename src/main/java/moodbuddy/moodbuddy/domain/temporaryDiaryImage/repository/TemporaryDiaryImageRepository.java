package moodbuddy.moodbuddy.domain.temporaryDiaryImage.repository;

import moodbuddy.moodbuddy.domain.temporaryDiaryImage.entity.TemporaryDiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryDiaryImageRepository extends JpaRepository<TemporaryDiaryImage, Long> {
}
