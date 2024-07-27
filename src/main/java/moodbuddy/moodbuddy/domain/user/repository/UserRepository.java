package moodbuddy.moodbuddy.domain.user.repository;

import jakarta.persistence.LockModeType;
import moodbuddy.moodbuddy.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//    @Modifying
//    @Transactional
//    @Query("update User u set u.userLetterNums = :letterNums where u.userId = :userId")
//    void updateLetterNumsById(@Param("userId") Long userId, @Param("letterNums") Integer letterNums);

    @Query("select u from User u where u.kakaoId = :kakaoId")
    Optional<User> findByKakaoId(@Param("kakaoId") Long kakaoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.kakaoId = :kakaoId")
    Optional<User> findByKakaoIdWithPessimisticLock(@Param("kakaoId") Long kakaoId);

    @Modifying
    @Transactional
    @Query("update User u set u.userCurDiaryNums = :curDiaryNums where u.userId = :userId")
    void updateCurDiaryNumsById(@Param("userId") Long userId, @Param("curDiaryNums") Integer curDiaryNums);

    @Modifying
    @Transactional
    @Query("update User u set u.userLastDiaryNums = :curDiaryNums where u.userId = :userId")
    void updateLastDiaryNumsById(@Param("userId") Long userId, @Param("curDiaryNums") Integer curDiaryNums);

    @Modifying
    @Transactional
    @Query("update User u set u.letterAlarm = :letterAlarm where u.kakaoId = :kakaoId")
    void updateLetterAlarmByKakaoId(@Param("kakaoId") Long kakaoId, @Param("letterAlarm") boolean letterAlarm);

}
