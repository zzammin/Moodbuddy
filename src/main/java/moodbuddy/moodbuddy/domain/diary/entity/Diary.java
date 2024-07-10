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
@Table(name = "diary")
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "diary_title", nullable = false, columnDefinition = "varchar(255)")
    private String diaryTitle;

    @Column(name = "diary_date", nullable = false, columnDefinition = "datetime")
    private LocalDateTime diaryDate;

//    @Lob
    @Column(name = "diary_content", nullable = false, columnDefinition = "text")
    private String diaryContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_weather", nullable = false, columnDefinition = "varchar(255)")
    private DiaryWeather diaryWeather;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_emotion", columnDefinition = "varchar(255)")
    private DiaryEmotion diaryEmotion;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_status", nullable = false, columnDefinition = "varchar(255)")
    private DiaryStatus diaryStatus;

    @Column(name = "diary_summary", columnDefinition = "varchar(255)")
    private String diarySummary;

    @Column(name = "kakao_id", columnDefinition = "bigint")
    private Long kakaoId;

    public void updateDiary(String title, LocalDateTime date, String content, DiaryWeather weather, String diarySummary) {
        this.diaryTitle = title;
        this.diaryDate = date;
        this.diaryContent = content;
        this.diaryWeather = weather;
        this.diarySummary = diarySummary;
        this.diaryStatus = DiaryStatus.PUBLISHED;
    }

    public void setDiaryEmotion(DiaryEmotion diaryEmotion) {
        this.diaryEmotion = diaryEmotion;
    }
}