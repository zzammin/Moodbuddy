package moodbuddy.moodbuddy.domain.diary.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Column(name = "diary_title", nullable = false, length = 255)
    private String diaryTitle;

    @Column(name = "diary_date", nullable = false)
    private LocalDateTime diaryDate;

    @Lob
    @Column(name = "diary_content")
    private String diaryContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_weather", nullable = false, length = 255)
    private DiaryWeather diaryWeather;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_emotion", nullable = false, length = 255)
    private DiaryEmotion diaryEmotion;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_status", nullable = false)
    private DiaryStatus diaryStatus;

    @Column(name = "diary_summary", nullable = false, length = 255)
    private String diarySummary;

    @Column(name = "user_email", nullable = false, length = 255)
    private String userEmail; // ManyToOne으로 User와 Diary를 연결하는 것이 아닌, userEmail을 칼럼으로 넣음

    protected Diary() {

    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
}
