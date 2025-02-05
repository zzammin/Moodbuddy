package moodbuddy.moodbuddy.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "bigint")
    private Long userId;

    @Column(name = "user_role", columnDefinition = "varchar(50)")
    private String userRole;

    @Column(name = "nickname", columnDefinition = "varchar(50)")
    private String nickname;

    @Column(name = "kakao_id", columnDefinition = "bigint")
    private Long kakaoId;

    @Column(name = "alarm", columnDefinition = "tinyint")
    private Boolean alarm;

    @Column(name = "alarm_time", columnDefinition = "varchar(10)")
    private String alarmTime;

    @Column(name = "letter_alarm", columnDefinition = "tinyint")
    private Boolean letterAlarm;

    @Column(name = "phone_number", columnDefinition = "varchar(20)")
    private String phoneNumber;

    @Column(name = "birthday", columnDefinition ="varchar(10)")
    private String birthday;

    @Column(name = "gender", columnDefinition = "tinyint")
    private Boolean gender;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @Column(name = "deleted", columnDefinition = "tinyint")
    private Boolean deleted;

    @Column(name = "access_token", columnDefinition = "text")
    private String accessToken;

    @Column(name = "access_token_expired_at", columnDefinition = "date")
    private LocalDate accessTokenExpiredAt;

    @Column(name = "refresh_token", columnDefinition = "text")
    private String refreshToken;

    @Column(name = "refresh_token_expired_at", columnDefinition = "date")
    private LocalDate refreshTokenExpiredAt;

    @Column(name = "user_cur_diary_nums", columnDefinition = "int")
    private Integer userCurDiaryNums;

    @Column(name = "user_last_diary_nums", columnDefinition = "int")
    private Integer userLastDiaryNums;

    @Column(name = "user_letter_nums", columnDefinition = "int")
    private Integer userLetterNums;

    @Column(name = "check_today_diary")
    private Boolean checkTodayDairy;

    public void setCheckTodayDiary(Boolean checkTodayDairy) {
        this.checkTodayDairy = checkTodayDairy;
    }
    public void plusUserNumCount() {
        userCurDiaryNums++;
        userLetterNums++;
    }
    public void minusUserNumCount() {
        userCurDiaryNums--;
        userLetterNums--;
    }
}
