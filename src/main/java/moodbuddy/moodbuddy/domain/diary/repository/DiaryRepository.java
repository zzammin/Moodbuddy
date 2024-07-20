package moodbuddy.moodbuddy.domain.diary.repository;

import moodbuddy.moodbuddy.domain.diary.dto.request.DiarySummaryVo;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.domain.diary.entity.DiaryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {
//    @Query(value = "SELECT * FROM diary WHERE kakao_id = :kakaoId AND DATE_FORMAT(diary_date, '%Y-%m') = :month", nativeQuery = true)
//    List<Diary> findByKakaoIdAndMonth(@Param("kakaoId") Long kakaoId, @Param("year") String year, @Param("month") String month);

    @Query(value = "SELECT * FROM diary WHERE kakao_id = :kakaoId AND DATE_FORMAT(diary_date, '%Y-%m') = :month", nativeQuery = true)
    List<Diary> findByKakaoIdAndMonth(@Param("kakaoId") Long kakaoId, @Param("month") String month);

    @Query(value = "SELECT * FROM diary WHERE kakao_id = :kakaoId AND DATE_FORMAT(diary_date, '%Y-%m-%d') = :day", nativeQuery = true)
    Optional<Diary> findByKakaoIdAndDay(@Param("kakaoId") Long kakaoId, @Param("day") String day);

    // diaryId 기반으로 삭제하기
    List<Diary> findAllById(Iterable<Long> ids);

    //사용자가 제일 최근에 쓴 일기 요약본 출력
    @Query(value = "SELECT * FROM diary WHERE kakao_id = :kakaoId ORDER BY diary_date DESC LIMIT 1", nativeQuery = true)
    Optional<Diary> findDiarySummaryById(@Param("kakaoId") Long kakaoId);

    // 오늘 작성한 일기가 있는지 확인
    Optional<Diary> findByDiaryDateAndKakaoId(LocalDate diaryDate, Long kakaoId);
    Optional<Diary> findByDiaryDateAndKakaoIdAndDiaryStatus(LocalDate diaryDate, Long kakaoId, DiaryStatus diaryStatus);
    List<Diary> findAllByDiaryDateAndKakaoIdAndDiaryStatus(LocalDate diaryDate, Long kakaoId, DiaryStatus diaryStatus);

    @Query("SELECT d FROM Diary d WHERE d.kakaoId = :kakaoId AND YEAR(d.diaryDate) = :year AND MONTH(d.diaryDate) = :month")
    List<Diary> findDiaryEmotionByKakaoIdAndMonth(@Param("kakaoId") Long kakaoId, @Param("year") int year, @Param("month") int month);

    @Query("SELECT d FROM Diary d WHERE d.kakaoId = :kakaoId AND YEAR(d.diaryDate) = :year AND d.diaryStatus = :status")
    List<Diary> findAllByYear(@Param("kakaoId") Long kakaoId, @Param("year") int year, @Param("status") DiaryStatus status);

    @Query("SELECT d FROM Diary d WHERE d.kakaoId = :kakaoId AND d.diaryEmotion is not null")
    List<Diary> findDiaryEmotionAllByKakaoId(@Param("kakaoId") Long kakaoId);

}