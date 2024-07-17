package moodbuddy.moodbuddy.domain.monthcomment.repository;

import moodbuddy.moodbuddy.domain.monthcomment.entity.MonthComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MonthCommentRepository extends JpaRepository<MonthComment,Long> {

    @Query("select mc.commentContent from MonthComment mc where mc.kakaoId = :kakaoId and mc.commentDate = :formattedMonth")
    String findCommentByKakaoIdAndMonth(@Param("kakaoId") Long kakaoId, @Param("formattedMonth") String formattedMonth);
}
