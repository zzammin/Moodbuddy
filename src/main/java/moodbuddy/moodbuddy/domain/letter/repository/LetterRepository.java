package moodbuddy.moodbuddy.domain.letter.repository;

import moodbuddy.moodbuddy.domain.letter.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter, Long> {
    @Query("select l from Letter l where l.user.kakaoId = :kakaoId")
    List<Letter> findByKakaoId(@Param("kakaoId") Long kakaoId);

    @Query("select l from Letter l where l.user.kakaoId = :kakaoId and l.letterDate = :letterDate")
    Optional<Letter> findByKakaoIdAndDate(@Param("kakaoId") Long kakaoId, @Param("letterDate") LocalDateTime letterDate);

    @Modifying
    @Query("update Letter l set l.letterAnswerContent = :answer where l.user.kakaoId = :kakaoId")
    void updateAnswerByKakaoId(Long kakaoId, String answer);

    @Query("select l from Letter l where l.id = :letterId and l.user.kakaoId = :kakaoId")
    Optional<Letter> findByIdAndKakaoId(@Param("letterId") Long letterId, @Param("kakaoId") Long kakaoId);
}