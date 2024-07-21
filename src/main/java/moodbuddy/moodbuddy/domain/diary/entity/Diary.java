package moodbuddy.moodbuddy.domain.diary.entity;

import jakarta.persistence.*;
import lombok.*;
import moodbuddy.moodbuddy.domain.diary.dto.request.DiaryReqUpdateDTO;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

import java.awt.*;
import java.time.LocalDate;
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

    @Column(name = "diary_date", nullable = false)
    private LocalDate diaryDate;

//    @Lob
    @Column(name = "diary_content", nullable = false, columnDefinition = "text")
    private String diaryContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_weather", nullable = false, columnDefinition = "varchar(10)")
    private DiaryWeather diaryWeather;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_emotion", columnDefinition = "varchar(10)")
    private DiaryEmotion diaryEmotion;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_status", nullable = false, columnDefinition = "varchar(10)")
    private DiaryStatus diaryStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_subject", columnDefinition = "varchar(10)")
    private DiarySubject diarySubject;

    @Column(name = "diary_summary", columnDefinition = "varchar(255)")
    private String diarySummary;

    @Column(name = "kakao_id", columnDefinition = "bigint")
    private Long kakaoId;

    @Column(name = "diary_book_mark_check", columnDefinition = "varchar(20)")
    private Boolean diaryBookMarkCheck; // 북마크 여부

    /** 추가 칼럼 **/
    @Enumerated(EnumType.STRING)
    @Column(name = "diary_font", columnDefinition = "varchar(10)")
    private DiaryFont diaryFont;

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_font_size", columnDefinition = "varchar(10)")
    private DiaryFontSize diaryFontSize;

    /** sql문 **/
    // ALTER TABLE diary
    // ADD COLUMN diary_font VARCHAR(10),
    // ADD COLUMN diary_font_size VARCHAR(10);

    public void updateDiary(DiaryReqUpdateDTO diaryReqUpdateDTO, String diarySummary, DiarySubject diarySubject) {
        this.diaryTitle = diaryReqUpdateDTO.getDiaryTitle();
        this.diaryDate = diaryReqUpdateDTO.getDiaryDate();
        this.diaryContent = diaryReqUpdateDTO.getDiaryContent();
        this.diaryWeather = diaryReqUpdateDTO.getDiaryWeather();
        this.diarySummary = diarySummary;
        this.diaryStatus = DiaryStatus.PUBLISHED;
        this.diarySubject = diarySubject;
        this.diaryFont = diaryReqUpdateDTO.getDiaryFont();
        this.diaryFontSize = diaryReqUpdateDTO.getDiaryFontSize();
    }

    public void setDiaryEmotion(DiaryEmotion diaryEmotion) {
        this.diaryEmotion = diaryEmotion;
    }
    public void setDiaryBookMarkCheck(Boolean diaryBookMarkCheck) { this.diaryBookMarkCheck = diaryBookMarkCheck; }
}