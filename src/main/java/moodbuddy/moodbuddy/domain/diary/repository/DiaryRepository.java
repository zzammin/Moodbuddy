package moodbuddy.moodbuddy.domain.diary.repository;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query(value = "select * from Diary d where d.userId = :userId and TO_CHAR(d.createdTime, 'yyyyMM') LIKE :month", nativeQuery = true)
    List<Diary> findByUserIdAndMonth(@Param("userId") Long userId, @Param("month") String month);
}
