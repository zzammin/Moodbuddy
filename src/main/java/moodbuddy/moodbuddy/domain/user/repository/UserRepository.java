package moodbuddy.moodbuddy.domain.user.repository;

import moodbuddy.moodbuddy.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Transactional
    @Query("update User u set u.userLetterNums = :letterNums where u.kakaoId = :kakaoId")
    void updateLetterNumsByKakaoId(@Param("kakaoId") Long kakaoId, @Param("letterNums") Integer letterNums);

    @Query("select u from User u where u.kakaoId = :kakaoId")
    Optional<User> findByKakaoId(@Param("kakaoId") Long kakaoId);

    @Modifying
    @Transactional
    @Query("update User u set u.fcmToken = :fcmToken where u.kakaoId = :kakaoId")
    void updateFcmTokenByKakaoId(@Param("kakaoId") Long kakaoId, @Param("fcmToken") String fcmToken);

    @Modifying
    @Transactional
    @Query("update User u set u.userCurDiaryNums = :curDiaryNums where u.kakaoId = :kakaoId")
    void updateCurDiaryNumsByKakaoId(@Param("kakaoId") Long kakaoId, @Param("curDiaryNums") Integer curDiaryNums);

    @Modifying
    @Transactional
    @Query("update User u set u.userLastDiaryNums = :curDiaryNums where u.kakaoId = :kakaoId")
    void updateLastDiaryNumsByKakaoId(@Param("kakaoId") Long kakaoId, @Param("curDiaryNums") Integer curDiaryNums);
}
