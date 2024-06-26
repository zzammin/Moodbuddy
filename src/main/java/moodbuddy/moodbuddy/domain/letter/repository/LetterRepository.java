package moodbuddy.moodbuddy.domain.letter.repository;

import moodbuddy.moodbuddy.domain.letter.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter, Long> {
    @Query("select l from Letter l where l.user.id = :userId")
    List<Letter> findByUserId(@Param("userId") Long userId);

    @Query("select l from Letter l where l.id = :letterId and l.user.id = :userId")
    Optional<Letter> findByIdAndUserId(Long letterId, Long userId);
}
