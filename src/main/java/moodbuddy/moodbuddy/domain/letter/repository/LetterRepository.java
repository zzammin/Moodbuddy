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
    @Query("select l from Letter l where l.user.id = :userId")
    List<Letter> findByUserId(@Param("userId") Long userId);

    @Query("select l from Letter l where l.id = :letterId and l.user.id = :userId")
    Optional<Letter> findByIdAndUserId(@Param("letterId") Long letterId, @Param("userId") Long userId);

    @Query("select l from Letter l where l.user.id = :userId and l.letterDate = :letterDate")
    Optional<Letter> findByUserIdAndDate(@Param("userId") Long userId, @Param("letterDate") LocalDateTime letterDate);

    @Modifying
    @Query("update Letter l set l.letterAnswerContent = :answer where l.user.id = :userId")
    void updateAnswerById(Long userId, String answer);
}