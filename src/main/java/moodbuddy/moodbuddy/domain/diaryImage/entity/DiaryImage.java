package moodbuddy.moodbuddy.domain.diaryImage.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import moodbuddy.moodbuddy.domain.diary.entity.Diary;
import moodbuddy.moodbuddy.global.common.base.BaseEntity;

@Entity
@Getter
@Builder
public class DiaryImage extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "diary_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Column(name = "diary_img_url")
    private String diaryImgURL = "";

    protected DiaryImage() {

    }
}
