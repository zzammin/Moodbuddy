package moodbuddy.moodbuddy.domain.user.repository;

import moodbuddy.moodbuddy.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("update User u set u.userLetterNums = :letterNums where u.userId = :userId")
    void updateLetterNumsById(@Param("userId") Long userId, @Param("letterNums") int letterNums);
    public Optional<User> findByKakaoId(Long kakaoId);

}
