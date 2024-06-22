package moodbuddy.moodbuddy.domain.diary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
public class Diary extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "diary_id")
    private Long id;
    private String diaryTitle;
    private String diaryContent;
    @Enumerated(EnumType.STRING)
    private DiaryWeather diaryWeather;
    @Enumerated(EnumType.STRING)
    private DiaryEmotion diaryEmotion;
    private String diarySummary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}