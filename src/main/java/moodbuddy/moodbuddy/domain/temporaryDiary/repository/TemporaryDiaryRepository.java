package moodbuddy.moodbuddy.domain.temporaryDiary.repository;

import moodbuddy.moodbuddy.domain.temporaryDiary.entity.TemporaryDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryDiaryRepository extends JpaRepository<TemporaryDiary, Long> {
}
