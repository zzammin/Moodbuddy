package moodbuddy.moodbuddy.domain.diary.repository;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query(value = "select d from Diary d where d.userEmail = :userEmail and function('DATE_FORMAT', d.createdTime, '%Y-%m') like :month")
    List<Diary> findByUserEmailAndMonth(@Param("userEmail") String userEmail, @Param("month") String month);

    @Query("select d from Diary d where d.userEmail = :userEmail and function('DATE_FORMAT', d.createdTime, '%Y-%m-%d') like :day")
    Optional<Diary> findByUserEmailAndDay(@Param("userEmail") String userEmail, @Param("day") String day);
}
