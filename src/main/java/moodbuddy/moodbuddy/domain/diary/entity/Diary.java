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
    private DiaryWeather diaryWeather;
    private DiaryEmotion diaryEmotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private User user;
}
