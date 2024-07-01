package moodbuddy.moodbuddy.domain.diary.repository;

import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {
    @Query(value = "SELECT * FROM diary WHERE kakao_id = :kakaoId AND DATE_FORMAT(diary_date, '%Y-%m') = :month", nativeQuery = true)
    List<Diary> findByKakaoIdAndMonth(@Param("kakaoId") Long kakaoId, @Param("month") String month);

    @Query(value = "SELECT * FROM diary WHERE kakao_id = :kakaoId AND DATE_FORMAT(diary_date, '%Y-%m-%d') LIKE :day", nativeQuery = true)
    Optional<Diary> findByKakaoIdAndDay(@Param("kakaoId") Long kakaoId, @Param("day") LocalDateTime day);

    // diaryId 기반으로 삭제하기
    List<Diary> findAllById(Iterable<Long> ids);
}