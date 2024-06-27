package moodbuddy.moodbuddy.domain.user.repository;

import moodbuddy.moodbuddy.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("update User u set u.userLetterNums = :letterNums where u.id = :userId")
    void updateLetterNumsById(@Param("userId") Long userId, @Param("letterNums") int letterNums);
}
