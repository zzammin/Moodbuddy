package moodbuddy.moodbuddy.domain.monthcomment.repository;

import moodbuddy.moodbuddy.domain.monthcomment.entity.MonthComment;
import moodbuddy.moodbuddy.domain.user.dto.request.UserReqMonthCommentUpdateDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MonthCommentRepository extends JpaRepository<MonthComment,Long> {

    @Query("select mc from MonthComment mc where mc.kakaoId = :kakaoId and mc.commentDate = :formattedMonth")
    Optional<MonthComment> findCommentByKakaoIdAndMonth(@Param("kakaoId") Long kakaoId, @Param("formattedMonth") String formattedMonth);

    @Modifying
    @Transactional
    @Query("update MonthComment mc set mc.commentContent = :monthComment where mc.kakaoId = :kakaoId and mc.commentDate = :chooseMonth")
    void updateCommentByKakaoIdAndMonth(@Param("kakaoId") Long kakaoId, @Param("chooseMonth") String chooseMonth, @Param("monthComment") String monthComment);
}
