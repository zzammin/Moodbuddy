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
    private String userEmail; // ManyToOne으로 User와 Diary를 연결하는 것이 아닌, userEmail을 칼럼으로 넣음

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
}