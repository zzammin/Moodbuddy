package moodbuddy.moodbuddy.domain.profile.repository;

import moodbuddy.moodbuddy.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("select p from Profile p where p.kakaoId = :kakaoId")
    Optional<Profile> findByKakaoId(@Param("kakaoId") Long kakaoId);
}
