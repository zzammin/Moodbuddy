package moodbuddy.moodbuddy.domain.diary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moodbuddy.moodbuddy.domain.user.entity.User;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "diary")
public class Diary extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "diary_id")
    private Long id;
    private Long userId;
    private String diaryTitle;
    private String diaryContent;
    private DiaryWeather diaryWeather;
    private DiaryEmotion diaryEmotion;
    private String diarySummary;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
}