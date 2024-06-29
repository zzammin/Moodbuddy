package moodbuddy.moodbuddy.domain.diary.entity;

import jakarta.persistence.*;
import lombok.*;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "diary_title", nullable = false, columnDefinition = "varchar" ,length = 255)
    private String diaryTitle;

    @Column(name = "diary_date", nullable = false, columnDefinition = "datetime")
    private LocalDateTime diaryDate;

    @Lob
    @Column(name = "diary_content")
    private String diaryContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_weather", nullable = false ,columnDefinition = "tinyint")
    private DiaryWeather diaryWeather;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_emotion", nullable = false, columnDefinition = "tinyint")
    private DiaryEmotion diaryEmotion;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_status", nullable = false)
    private DiaryStatus diaryStatus;

    @Column(name = "diary_summary", nullable = false, columnDefinition = "mediumtext")
    private String diarySummary;

    @Column(name = "user_id",columnDefinition = "bigint")
    private Long userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    public void updateDiary(String title, LocalDateTime date, String content, DiaryWeather weather) {
        this.diaryTitle = title;
        this.diaryDate = date;
        this.diaryContent = content;
        this.diaryWeather = weather;
    }
}
