package moodbuddy.moodbuddy.domain.diary.repository;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query(value = "SELECT d FROM Diary d WHERE d.user_id = :userId AND DATE_FORMAT(d.diary_date, '%Y-%m') = :month", nativeQuery = true)
    List<Diary> findByUserIdAndMonth(@Param("userId") Long userId, @Param("month") String month);

    @Query(value = "select d from Diary d where d.user_id = :userId and function('DATE_FORMAT', d.diary_date, '%Y-%m-%d') like :day", nativeQuery = true)
    Optional<Diary> findByUserIdAndDay(@Param("userId") Long userId, @Param("day") LocalDateTime day);
}
