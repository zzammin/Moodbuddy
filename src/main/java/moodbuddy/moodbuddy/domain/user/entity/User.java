package moodbuddy.moodbuddy.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

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

    @Column(name = "alarm_time", columnDefinition = "datetime")
    private LocalDateTime alarmTime;

    @Column(name = "birthday", columnDefinition = "datetime(6)")
    private LocalDateTime birthday;

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
}
