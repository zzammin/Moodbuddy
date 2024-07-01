package moodbuddy.moodbuddy.domain.user.repository;

import moodbuddy.moodbuddy.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("update User u set u.userLetterNums = :letterNums where u.kakaoId = :kakaoId")
    void updateLetterNumsByKakaoId(@Param("kakaoId") Long kakaoId, @Param("letterNums") int letterNums);

    @Query("select u from User u where u.kakaoId = :kakaoId")
    Optional<User> findByKakaoId(@Param("kakaoId") Long kakaoId);
}
